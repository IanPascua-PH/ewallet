package com.api.ewallet.integration.getfriendlist;

import com.api.ewallet.EWalletApplicationTests;
import com.api.ewallet.util.TestUtil;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;

public abstract class GetFriendListTestDataFactory extends EWalletApplicationTests {

    protected static final String URI = "/v1/api/wallet/getFriendList";;
    protected static final String EXTERNAL_URI = "/users";
    protected static final String RESPONSE = "_response.json";

    protected static final HttpResponse EXTERNAL_RESPONSE = HttpResponse.response()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(getJsonResponse("external_success"));

    protected static final HttpResponse EXTERNAL_NOT_FOUND_RESPONSE = HttpResponse.response()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatusCode(404)
            .withBody(getJsonResponse("external_not_found"));

    protected static HttpRequest createExternalRequest(String uri, String userId) {
        return HttpRequest.request(uri + userId)
                .withHeader(HttpHeaders.ACCEPT, "application/json, application/*+json")
                .withMethod(HttpMethod.GET.toString());
    }

    public static String getJsonResponse(String fileName){
        try {
            return TestUtil.getJsonFromFile(String.join("/", "getfriendlist", fileName + RESPONSE));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
