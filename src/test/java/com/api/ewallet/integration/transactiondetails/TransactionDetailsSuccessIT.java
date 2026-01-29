package com.api.ewallet.integration.transactiondetails;

import com.api.ewallet.integration.inquirebalance.InquireBalanceTestDataFactory;
import com.api.ewallet.model.entity.Transaction;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import com.api.ewallet.service.ExternalUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionDetailsSuccessIT extends TransactionDetailsTestDataFactory {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private ExternalUserService externalUserService;

    @Test
    void testTransactionDetailsSuccessIT() throws Exception {
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

        Transaction transaction = Transaction.builder()
                .transactionId("TXN001")
                .senderUserId("USER003")
                .recipientUserId("USER007")
                .referenceId("REF001")
                .amount(BigDecimal.valueOf(100.00))
                .currency("PHP")
                .transactionStatus("1")
                .status("1")
                .note("Test transaction")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser));

        when(transactionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(transaction));

        when(externalUserService.getByUserId("USER003")).thenReturn(ExternalUserResponse.builder()
                .name("User Three")
                .username("user3")
                .email("user3@example.com")
                .phone("09231234567")
                .build());
        when(externalUserService.getByUserId("USER007")).thenReturn(ExternalUserResponse.builder()
                .name("User Seven")
                .username("user7")
                .email("user7@example.com")
                .phone("09231234567")
                .build());

        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER003")).respond(EXTERNAL_RESPONSE);
        mockServer.when(createExternalRequest(EXTERNAL_URI, "USER007")).respond(EXTERNAL_RESPONSE);

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER003")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.transactionId", not(emptyString())))
                .andExpect(jsonPath("$.referenceId", not(emptyString())))
                .andExpect(jsonPath("$.amount", not(emptyString())))
                .andExpect(jsonPath("$.currency", not(emptyString())))
                .andExpect(jsonPath("$.status", not(emptyString())))
                .andExpect(jsonPath("$.description", not(emptyString())))
                .andExpect(jsonPath("$.senderInfo").isNotEmpty())
                .andExpect(jsonPath("$.recipientInfo").isNotEmpty())
                .andDo(print())
                .andReturn();
    }
}
