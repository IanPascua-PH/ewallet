package com.api.ewallet.service.api;

import com.api.ewallet.model.wallet.SendMoneyRequest;
import com.api.ewallet.model.wallet.SendMoneyResponse;

public interface SendMoneyService {

    SendMoneyResponse initiateSendMoney(String userId, SendMoneyRequest request);

}
