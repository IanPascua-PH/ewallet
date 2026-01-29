package com.api.ewallet.controller;

import com.api.ewallet.model.wallet.*;
import com.api.ewallet.model.wallet.FriendListResponse.Friend;
import com.api.ewallet.service.WalletService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/wallet")
public class WalletController implements WalletControllerApi {

    private final WalletService walletService;

    @Override
    @GetMapping("/getFriendList")
    public ResponseEntity<FriendListResponse> getFriendList(
            @Parameter(description = "User ID passed in header", required = true)
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /api/friend - userId: {}", userId);
        List<Friend> friendList = walletService.getFriendList(userId);
        FriendListResponse response = FriendListResponse.builder()
                .friendList(friendList)
                .build();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/inquireBalance")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(
            @Parameter(description = "User ID passed in header", required = true)
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /api/wallet/balance - userId: {}", userId);

        return ResponseEntity.ok(walletService.getWalletBalance(userId));
    }

    @Override
    @GetMapping("/transactionDetails/{txnId}")
    public ResponseEntity<TransactionResponse> getTransactionDetails(
            @Parameter(description = "User ID passed in header", required = true) @RequestHeader("X-User-Id") String userId,
            @Parameter(description = "Transaction ID", required = true) @PathVariable String txnId) {
        log.info("GET /api/wallet/transactionDetails - userId: {}", userId);

        return ResponseEntity.ok(walletService.getTransactionDetails(userId, txnId));
    }

    @Override
    @GetMapping("/transactionHistory")
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(
            @Parameter(description = "User ID passed in header", required = true) @RequestHeader("X-User-Id") String userId) {
        log.info("GET /api/wallet/transactionHistory - userId: {}", userId);

        List<TransactionResponse> transactions = walletService.getTransactionHistory(userId);
        TransactionHistoryResponse response = TransactionHistoryResponse.builder()
                .transactions(transactions)
                .build();
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/sendMoney")
    public ResponseEntity<SendMoneyResponse> initiateSendMoney(
            @Parameter(description = "User ID passed in header", required = true) @RequestHeader("X-User-Id") String userId,
            @Parameter(description = "Send Money Request", required = true) @Valid @RequestBody SendMoneyRequest request) {
        log.info("POST /api/wallet/sendMoney - userId: {}", userId);

        return ResponseEntity.ok(walletService.initiateSendMoney(userId, request));
    }

}
