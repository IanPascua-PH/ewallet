package com.api.ewallet.integration.sendmoney;

import com.api.ewallet.configuration.properties.WalletConfigProperties;
import com.api.ewallet.model.entity.Transaction;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.entity.Wallet;
import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SendMoneyErrorIT extends SendMoneyTestDataFactory{

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private WalletConfigProperties walletConfigProperties;

    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void testUserNotFoundIT() throws Exception{
        when(userRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        testBusinessError("NONEXISTENT", "EWLBE404", "Not Found Error", "404", "User not found");
    }

    @Test
    void testRecipientUnverifiedKycStatusErrorIT() throws Exception{
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
                .id(1L)
                .userId("USER001")
                .fullName("User One")
                .username("")
                .email("user1@example.com")
                .phoneNumber("09171234567")
                .kycStatus("0")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Address 1")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser))
                .thenReturn(Optional.of(recipientUser));

        testInvalidError("USER003", "EWLBE608", "Bad Request", "400", "Invalid transaction. Recipient is not verified yet.");
    }

    @Test
    void testUserUnverifiedKycStatusErrorIT() throws Exception{
        User mockUser = User.builder()
                .id(1L)
                .userId("USER003")
                .fullName("User Three")
                .username("user3")
                .email("user3@example.com")
                .phoneNumber("09231234567")
                .kycStatus("0")
                .dateOfBirth(LocalDate.of(1992, 3, 3))
                .address("Address 3")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(userRepository.findOne(any(Specification.class))).thenReturn(Optional.of(mockUser));

        testInvalidError("USER007", "EWLBE608", "Bad Request", "400", "Invalid transaction. User is not verified yet.");
    }

    @Test
    void testInsufficientBalanceErrorIT() throws Exception{
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
                .id(1L)
                .userId("USER001")
                .fullName("User One")
                .username("")
                .email("user1@example.com")
                .phoneNumber("09171234567")
                .kycStatus("1")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Address 1")
                .status("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Wallet wallet = Wallet.builder()
                .id(3L)
                .walletId("WALLET003")
                .userId("USER003")
                .balance(BigDecimal.valueOf(50.00))
                .currency("PHP")
                .status("1")
                .build();
        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser))
                .thenReturn(Optional.of(recipientUser));
        when(walletRepository.findByUserId(anyString()))
                .thenReturn(Optional.of(wallet));

        testInvalidError("USER003", "EWLBE609", "Bad Request", "400", "Insufficient balance");
    }

    @Test
    void testSameWalletErrorIT() throws Exception{
        User mockUser = User.builder()
                .id(1L)
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
        when(userRepository.findOne(any(Specification.class))).thenReturn(Optional.of(mockUser));

        testInvalidError("USER003", "EWLBE608", "Bad Request", "400", "Invalid transaction. Unable to send to own wallet.");
    }

    @Test
    void testSecurityErrorNullIT() throws Exception{
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.errorDetails[0].code").value("EWLBE401"))
                .andExpect(jsonPath("$.errorDetails[0].message").value("Missing X-User-Id header"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testSecurityErrorEmptyIT() throws Exception{
        mockMvc.perform(post(URI).headers(createJsonHeaders(""))
                        .content(mockRequest("", "09231234567"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.errorDetails[0].code").value("EWLBE401"))
                .andExpect(jsonPath("$.errorDetails[0].message").value("Missing X-User-Id header"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testSecurityErrorWhitespaceIT() throws Exception{
        mockMvc.perform(post(URI).headers(createJsonHeaders("   "))
                        .content(mockRequest("", "09231234567"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.errorDetails[0].code").value("EWLBE401"))
                .andExpect(jsonPath("$.errorDetails[0].message").value("Missing X-User-Id header"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testTransactionFailedIT() throws Exception {
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
        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of());
        when(walletConfigProperties.getDailyLimit()).thenReturn(BigDecimal.valueOf(10000.00));
        when(walletRepository.save(any(Wallet.class))).thenThrow(new RuntimeException("DB save failed"));

        mockMvc.perform(post(URI).headers(createJsonHeaders("USER003"))
                        .content(mockRequest("", "09231234567"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.errorDetails[0].code").value("EWLSE610"))
                .andExpect(jsonPath("$.errorDetails[0].message").value("Transaction failed"))
                .andDo(print())
                .andReturn();

        // Verify that transaction was updated to FAILED
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        List<Transaction> capturedTransactions = transactionCaptor.getAllValues();
        assertThat(capturedTransactions.get(0).getTransactionStatus()).isEqualTo("2"); // FAILED
    }

    @Test
    void testDailyLimitExceededErrorIT() throws Exception{
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
        Transaction tx1 = Transaction.builder()
                .amount(BigDecimal.valueOf(400.00))
                .build();
        Transaction tx2 = Transaction.builder()
                .amount(BigDecimal.valueOf(500.00))
                .build();
        when(userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.of(senderUser))
                .thenReturn(Optional.of(recipientUser));
        when(walletRepository.findByUserId(anyString()))
                .thenReturn(Optional.of(wallet));
        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(tx1, tx2));
        when(walletConfigProperties.getDailyLimit()).thenReturn(BigDecimal.valueOf(1000.00));

        testInvalidError("USER003", "EWLBE619", "Bad Request", "400", "Exceeded daily transfer limit");
    }

    protected void testBusinessError(String userId, String errorCode, String error, String responseCode, String errorMessage) throws Exception {
        mockMvc.perform(post(URI).headers(createJsonHeaders(userId))
                        .content(mockRequest("", "09231234567"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(responseCode))
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.errorDetails[0].code").value(errorCode))
                .andExpect(jsonPath("$.errorDetails[0].message").value(errorMessage))
                .andDo(print())
                .andReturn();
    }

    protected void testInvalidError(String userId, String errorCode, String error, String responseCode, String errorMessage) throws Exception {
        mockMvc.perform(post(URI).headers(createJsonHeaders(userId))
                        .content(mockRequest("", "09231234567"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(responseCode))
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.errorDetails[0].code").value(errorCode))
                .andExpect(jsonPath("$.errorDetails[0].message").value(errorMessage))
                .andDo(print())
                .andReturn();
    }
}
