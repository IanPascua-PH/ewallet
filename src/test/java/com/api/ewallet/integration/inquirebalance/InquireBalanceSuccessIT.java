package com.api.ewallet.integration.inquirebalance;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InquireBalanceSuccessIT extends InquireBalanceTestDataFactory {

    @Test
    void testInquireBalanceSuccessIT() throws Exception {

        mockServer.when(createExternalRequest(EXTERNAL_URI, "/1")).respond(EXTERNAL_RESPONSE);

        mockMvc.perform(get(URI).headers(createJsonHeaders("USER001")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.walletId").value("WALLET001"))
                .andExpect(jsonPath("$.availableBalance").isNotEmpty())
                .andExpect(jsonPath("$.limits").isNotEmpty())
                .andExpect(jsonPath("$.walletStatus").value("Active"))
                .andDo(print())
                .andReturn();
    }
}
