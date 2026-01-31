package com.api.ewallet.service.api;

import com.api.ewallet.model.wallet.WalletBalanceResponse;

public interface InquireBalanceService {

    WalletBalanceResponse inquireBalance(String userId);

}
