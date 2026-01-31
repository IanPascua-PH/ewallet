package com.api.ewallet.service.ws;


import com.api.ewallet.model.external.ExternalUserResponse;

public interface ExternalUserService {

  ExternalUserResponse getByUserId(String userId);

}
