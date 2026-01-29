package com.api.ewallet.integration.getfriendlist;

import org.junit.jupiter.api.Test;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpResponse;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetFriendListErrorIT extends GetFriendListTestDataFactory {

    @Test
    void testUserNotFoundIT() throws Exception{
        mockServer.when(createExternalRequest(EXTERNAL_URI, "11")).respond(EXTERNAL_NOT_FOUND_RESPONSE);

        testBusinessError("NONEXISTENT", "EWLBE404", "Not Found Error", "404", "userId not found");
    }

    @Test
    void testExternalServiceErrorIT() throws Exception{
        mockServer.when(createExternalRequest(EXTERNAL_URI, "12")).error(HttpError.error().withDropConnection(true));

        testServiceError("EWLSE999", "Internal Server Error", "500", "Backend service error");
    }

    @Test
    void testExternalTimeoutErrorIT() throws Exception{
        mockServer.when(createExternalRequest(EXTERNAL_URI, "12")).respond(HttpResponse.response().withDelay(Delay.milliseconds(5001)));

        testServiceError("EWLSE999", "Internal Server Error", "500", "Backend service error");
    }

    @Test
    void testSecurityErrorNullIT() throws Exception{
        mockMvc.perform(get(URI)
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

    protected void testServiceError(String errorCode, String error, String responseCode, String errorMessage) throws Exception {
        mockMvc.perform(get(URI).headers(createJsonHeaders("USER001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").value(responseCode))
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.errorDetails[0].code").value(errorCode))
                .andExpect(jsonPath("$.errorDetails[0].message").value(errorMessage))
                .andDo(print())
                .andReturn();

    }
}
