package com.api.ewallet;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = EWalletApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = MockServerInitializer.class)
@Getter
public abstract class EWalletApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    @Qualifier("clientServer")
    protected ClientAndServer mockServer;

    @BeforeEach
    public void beforeEach(){
        mockServer.reset();
    }

    protected HttpHeaders createJsonHeaders(String userId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Id", userId);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    protected ResultActions performServiceApiCall(MockHttpServletRequestBuilder httpRequest) throws Exception {
        return this.getMockMvc()
                .perform(httpRequest)
                .andDo(print());
    }



}
