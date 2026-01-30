package com.api.ewallet.integration.transactionhistory;

import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionHistoryErrorIT extends TransactionHistoryTestDataFactory {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void testUserNotFoundIT() throws Exception{
        when(userRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        testBusinessError("NONEXISTENT", "EWLBE404", "Not Found Error", "404", "User not found");
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
        mockMvc.perform(get(URI).headers(createJsonHeaders(""))
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
        mockMvc.perform(get(URI).headers(createJsonHeaders("   "))
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

    protected void testBusinessError(String userId, String errorCode, String error, String responseCode, String errorMessage) throws Exception {
        mockMvc.perform(get(URI).headers(createJsonHeaders(userId))
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
}
