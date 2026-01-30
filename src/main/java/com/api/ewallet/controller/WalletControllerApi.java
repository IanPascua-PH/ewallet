package com.api.ewallet.controller;

import com.api.ewallet.model.wallet.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "v1")
public interface WalletControllerApi {

    @Operation(description = "Get friend list for the user", tags = {"Get Friend List"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<FriendListResponse> getFriendList(String userId);

    @Operation(description = "Get wallet balance of the user", tags = {"Get Wallet Balance"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<WalletBalanceResponse> getWalletBalance(String userId);

    @Operation(description = "Transfer funds to another user", tags = {"Send Money"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<SendMoneyResponse> initiateSendMoney(String userId, SendMoneyRequest request);

    @Operation(description = "Get transaction detail of the user's transaction", tags = {"Get Transaction Details"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    ResponseEntity<TransactionResponse> getTransactionDetails(String userId, String txnId);

    @Operation(description = "Get transaction history of the user", tags = {"Get Transaction History"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<TransactionHistoryResponse> getTransactionHistory(String userId);
}
