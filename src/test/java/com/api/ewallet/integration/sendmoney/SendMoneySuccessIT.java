package com.api.ewallet.integration.sendmoney;

import com.api.ewallet.configuration.properties.WalletConfigProperties;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.entity.Wallet;
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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SendMoneySuccessIT extends SendMoneyTestDataFactory {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private WalletConfigProperties walletConfigProperties;

    @MockBean
    private ExternalUserService externalUserService;

    @Test
    void testSendMoneySuccessIT() throws Exception {
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
        User recipientUser = User.builder()
                .id(7L)
                .userId("USER007")
                .fullName("User Seven")
                .username("user7")
                .email("user7@example.com")
                .phoneNumber("09231234567")
                .kycStatus("1")
                .dateOfBirth(LocalDate.of(1996, 7, 7))
                .address("Address 7")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Wallet wallet = Wallet.builder()
                .id(3L)
                .walletId("WALLET003")
                .userId("USER003")
                .balance(BigDecimal.valueOf(1000.00))
                .currency("PHP")
                .status("1")
                .build();

        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser))
                .thenReturn(Optional.of(recipientUser));
        when(walletRepository.findByUserId(anyString()))
                .thenReturn(Optional.of(wallet));
        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(walletConfigProperties.getDailyLimit()).thenReturn(BigDecimal.valueOf(30000.00));
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

        mockMvc.perform(post(URI).headers(createJsonHeaders("USER003"))
                        .content(mockRequest("", "09231234567")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.transactionId", not(emptyString())))
                .andExpect(jsonPath("$.referenceId", not(emptyString())))
                .andExpect(jsonPath("$.status", not(emptyString())))
                .andExpect(jsonPath("$.timeStamp", not(emptyString())))
                .andDo(print())
                .andReturn();
    }
}
