package com.api.ewallet.service.api;

import com.api.ewallet.model.wallet.TransactionResponse;

import java.util.List;

public interface TransactionHistoryService {

    List<TransactionResponse> getTransactionHistory(String userId);

}
