package com.api.ewallet.integration.getfriendlist;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetFriendListSuccessIT extends GetFriendListTestDataFactory {

    @Test
    void testGetFriendListSuccessIT() throws Exception {

        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/users/.*")).respond(EXTERNAL_RESPONSE);

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER001")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.friendList").isNotEmpty())
                .andDo(print())
                .andReturn();
    }

}
