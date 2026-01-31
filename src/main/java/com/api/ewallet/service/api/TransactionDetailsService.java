package com.api.ewallet.service.api;

import com.api.ewallet.model.wallet.TransactionResponse;

public interface TransactionDetailsService {

    TransactionResponse getTransactionDetails(String userId, String transactionId);

}
