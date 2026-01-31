# EWallet Microservice

## Overview
This is a Spring Boot-based e-wallet microservice that provides functionalities for user management, wallet operations, and transactions. It includes features like getting friend lists, checking wallet balances, sending money, and retrieving transaction histories.

## Run Instructions

### Prerequisites
- Docker and Docker Compose installed on your machine.

### Steps to Run
1. Clone the repository.
2. Navigate to the project root directory (`ewallet`).
3. Run the following command to build and start the application along with the PostgreSQL database:
   ```
   docker-compose up --build
   ```
4. The application will be available at `http://localhost:8080`.
5. API documentation (Swagger) can be accessed at `http://localhost:8080/swagger-ui.html`.

### Stopping the Application
- Use `Ctrl+C` or run `docker-compose down` to stop the services.

## Design Choices

### Architecture
- **Microservice Architecture**: The application is designed as a standalone microservice, focusing on wallet-related operations.
- **Layered Architecture**: Follows a clean architecture with Controller, Service, Repository, and Entity layers.
- **RESTful APIs**: Uses REST principles for API design.
- **Database**: PostgreSQL for data persistence.
- **External Integrations**: Integrates with an external user service for additional user data.

### Key Technologies
- **Spring Boot**: For rapid development and embedded server.
- **Spring Data JPA**: For ORM and repository pattern.
- **Specifications**: Used for dynamic query building to avoid SQL injection and improve reusability.
- **MockServer**: For mocking external API calls in tests.
- **Lombok**: To reduce boilerplate code.
- **OpenAPI/Swagger**: For API documentation.

### Security
- Custom filter for user ID validation via headers.
- Exception handling with custom exceptions and consistent error responses.

### Transaction Management
- Uses Spring's `@Transactional` for managing database transactions.
- Implements pessimistic locking for concurrent wallet updates.
- Saves transactions with initial "PENDING" status, updates to "COMPLETED" on success or "FAILED" on error/scheduled cleanup.
- Scheduled job (configurable, every 5 minutes) marks stuck "PENDING" transactions (>3 minutes old) as "FAILED" for auditing.
- Database indexes on transaction_status, created_at, user_id for query optimization.

### Testing
- Integration tests with MockMvc and mocked repositories to avoid DB dependencies.
- Uses WireMock for external service mocking.

## API Specifications

The API is documented using OpenAPI/Swagger. Below is a summary of the endpoints:

### Endpoints

1. **GET /v1/api/wallet/getFriendList**
   - Description: Retrieve the friend list for a user.
   - Headers: X-User-Id (required)
   - Response: 200 OK with FriendListResponse.

2. **GET /v1/api/wallet/getWalletBalance**
   - Description: Get the wallet balance for a user.
   - Headers: X-User-Id (required)
   - Response: 200 OK with WalletBalanceResponse.

3. **POST /v1/api/wallet/sendMoney**
   - Description: Initiate a money transfer to another user.
   - Headers: X-User-Id (required)
   - Body: SendMoneyRequest (username or phoneNumber, amount, note)
   - Response: 200 OK with SendMoneyResponse.

4. **GET /v1/api/wallet/transactionDetails/{txnId}**
   - Description: Get details of a specific transaction.
   - Headers: X-User-Id (required)
   - Path Param: txnId
   - Response: 200 OK with TransactionResponse.

5. **GET /v1/api/wallet/transactionHistory**
   - Description: Get the transaction history for a user.
   - Headers: X-User-Id (required)
   - Response: 200 OK with TransactionHistoryResponse.

### Error Responses
- 400 Bad Request: Validation errors, invalid transactions.
- 401 Unauthorized: Missing or invalid X-User-Id header.
- 404 Not Found: User, wallet, or transaction not found.
- 500 Internal Server Error: Unexpected errors.

## Diagrams

### Class Diagram
```
+----------------+     +-----------------------+     +-----------------+
|  Controller    |     |       Service         |     |   Repository    |
+----------------+     +-----------------------+     +-----------------+
| WalletController| --> | FriendListService     | --> | UserRepository  |
|                 | --> | InquireBalanceService | --> | WalletRepository|
|                 | --> | SendMoneyService      | --> | TransactionRepo |
|                 | --> | TransactionDetailsSvc | --> | TransactionRepo |
|                 | --> | TransactionHistorySvc | --> | TransactionRepo |
+----------------+     +-----------------------+     +-----------------+
          |                       |                       |
          v                       v                       v
+----------------+     +-----------------+     +-----------------+
|   Entity       |     | Specification   |     |   Exception     |
+----------------+     +-----------------+     +-----------------+
| User           |     | UserSpec        |     | NotFoundException|
| Wallet         |     | WalletSpec      |     | InvalidTxnEx     |
| Transaction    |     | TransactionSpec |     | ...             |
+----------------+     +-----------------+     +-----------------+
```

### Sequence Diagram (Get Friend List)
1. User sends GET /getFriendList with userId header.
2. Controller calls service getFriendList(userId).
3. Service fetches current user, fetches all users, filters out current user.
4. Service maps users to friends using external service.
5. Service returns friend list.
6. Controller returns 200 OK with FriendListResponse.

### Sequence Diagram (Get Wallet Balance)
1. User sends GET /getWalletBalance with userId header.
2. Controller calls service getWalletBalance(userId).
3. Service fetches user and wallet, builds response.
4. Controller returns 200 OK with WalletBalanceResponse.

### Sequence Diagram (Send Money)
1. User sends POST /sendMoney with userId and request.
2. Controller validates header, calls service initiateSendMoney().
3. Service validates users, checks balance and daily limit.
4. Service creates transaction with "PENDING" status, performs transfer (deduct/credit), updates transaction to "COMPLETED".
5. Service fetches external data, builds response.
6. Controller returns 200 OK with SendMoneyResponse.

### Sequence Diagram (Get Transaction Details)
1. User sends GET /transactionDetails/{txnId} with userId header.
2. Controller calls service getTransactionDetails(userId, txnId).
3. Service fetches user and transaction, builds response with external data.
4. Controller returns 200 OK with TransactionResponse.

### Sequence Diagram (Get Transaction History)
1. User sends GET /transactionHistory with userId header.
2. Controller calls service getTransactionHistory(userId).
3. Service fetches user, fetches transactions, maps to responses with external data.
4. Controller returns 200 OK with TransactionHistoryResponse.
