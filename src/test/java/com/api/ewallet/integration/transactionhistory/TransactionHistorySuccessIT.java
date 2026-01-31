package com.api.ewallet.integration.transactionhistory;

import com.api.ewallet.model.entity.Transaction;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import com.api.ewallet.service.ws.ExternalUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionHistorySuccessIT extends TransactionHistoryTestDataFactory {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private ExternalUserService externalUserService;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    void testTransactionHistoryNoTransactionsSuccessIT() throws Exception {
        User senderUser = User.builder()
                .id(3L)
                .userId("USER003")
                .fullName("User Three")
                .username("user3")
                .email("user3@example.com")
                .phoneNumber("09231234567")
                .kycStatus("1")
                .dateOfBirth(LocalDate.of(1992, 3, 3))
                .address("Address 3")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser));

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of());

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER003")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(print())
                .andReturn();
    }

    @Test
    void testTransactionHistoryAsRecipientSuccessIT() throws Exception {
        User senderUser = User.builder()
                .id(3L)
                .userId("USER003")
                .fullName("User Three")
                .username("user3")
                .email("user3@example.com")
                .phoneNumber("09231234567")
                .kycStatus("1")
                .dateOfBirth(LocalDate.of(1992, 3, 3))
                .address("Address 3")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser));

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(
                Transaction.builder()
                        .id(101L)
                        .transactionId("TXN101")
                        .senderUserId("USER007")  // USER003 is recipient
                        .recipientUserId("USER003")
                        .amount(new BigDecimal("150.00"))
                        .currency("USD")
                        .status("COMPLETED")
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .updatedAt(LocalDateTime.now().minusDays(1))
                        .build()
        ));

        when(externalUserService.getByUserId("USER007")).thenReturn(ExternalUserResponse.builder()
                .name("User Seven")
                .username("user7")
                .email("user7@example.com")
                .phone("09231234567")
                .build());
        when(externalUserService.getByUserId("USER003")).thenReturn(ExternalUserResponse.builder()
                .name("User Three")
                .username("user3")
                .email("user3@example.com")
                .phone("09231234567")
                .build());

        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER007")).respond(EXTERNAL_RESPONSE);
        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER003")).respond(EXTERNAL_RESPONSE);

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER003")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.transactions").isNotEmpty())
                .andExpect(jsonPath("$.transactions[0].description").value("Receive Funds"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testTransactionHistoryAsSenderSuccessIT() throws Exception {
        User senderUser = User.builder()
                .id(3L)
                .userId("USER003")
                .fullName("User Three")
                .username("user3")
                .email("user3@example.com")
                .phoneNumber("09231234567")
                .kycStatus("1")
                .dateOfBirth(LocalDate.of(1992, 3, 3))
                .address("Address 3")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser));

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(
                Transaction.builder()
                        .id(101L)
                        .transactionId("TXN101")
                        .senderUserId("USER003")  // USER003 is sender
                        .recipientUserId("USER007")
                        .amount(new BigDecimal("150.00"))
                        .currency("USD")
                        .status("COMPLETED")
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .updatedAt(LocalDateTime.now().minusDays(1))
                        .build()
        ));

        when(externalUserService.getByUserId("USER007")).thenReturn(ExternalUserResponse.builder()
                .name("User Seven")
                .username("user7")
                .email("user7@example.com")
                .phone("09231234567")
                .build());
        when(externalUserService.getByUserId("USER003")).thenReturn(ExternalUserResponse.builder()
                .name("User Three")
                .username("user3")
                .email("user3@example.com")
                .phone("09231234567")
                .build());

        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER007")).respond(EXTERNAL_RESPONSE);
        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER003")).respond(EXTERNAL_RESPONSE);

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER003")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.transactions").isNotEmpty())
                .andExpect(jsonPath("$.transactions[0].description").value("Transfer Funds"))
                .andDo(print())
                .andReturn();
    }
}
