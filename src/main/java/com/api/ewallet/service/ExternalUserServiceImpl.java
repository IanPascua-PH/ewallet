package com.api.ewallet.service;

import com.api.ewallet.configuration.properties.ExternalUserConfigProperties;
import com.api.ewallet.exception.InternalServerException;
import com.api.ewallet.model.external.ExternalUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalUserServiceImpl implements ExternalUserService {

    private final RestTemplate restTemplate;
    private final ExternalUserConfigProperties configProperties;

    @Override
    public ExternalUserResponse getByUserId(String userId) {
        try {
            log.debug("Fetching external user data for userId: {}", userId);
            int numericId = Integer.parseInt(userId.replaceAll("\\D+", ""));

            String url = configProperties.getBasePath() + configProperties.getGetUsers() + "/" + numericId;
            ExternalUserResponse user = restTemplate.getForObject(url, ExternalUserResponse.class);
            log.debug("Successfully fetched user data: {}", user);
            return user;
        } catch (Exception ex) {
            throw new InternalServerException("Backend service error");
        }
    }

}
