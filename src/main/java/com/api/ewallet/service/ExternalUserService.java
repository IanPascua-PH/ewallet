package com.api.ewallet.service;


import com.api.ewallet.model.external.ExternalUserResponse;

public interface ExternalUserService {

  ExternalUserResponse getByUserId(String userId);

}
