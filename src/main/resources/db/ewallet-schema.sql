CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    kyc_status VARCHAR(1) NOT NULL DEFAULT '0',
    date_of_birth DATE,
    address VARCHAR(500),
    status VARCHAR(1) NOT NULL DEFAULT '1',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_id VARCHAR(20) NOT NULL UNIQUE,
    user_id VARCHAR(20) NOT NULL UNIQUE REFERENCES users(user_id),
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'PHP',
    status VARCHAR(1) NOT NULL DEFAULT '1',
    daily_limit DECIMAL(19, 2) NOT NULL DEFAULT 1000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT balance_non_negative CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(30) NOT NULL UNIQUE,
    sender_wallet_id VARCHAR(20) NOT NULL UNIQUE,
    recipient_wallet_id VARCHAR(20) NOT NULL UNIQUE,
    sender_user_id VARCHAR(20) NOT NULL UNIQUE,
    recipient_user_id VARCHAR(20) NOT NULL UNIQUE,
    reference_id VARCHAR(30) NOT NULL UNIQUE,
    amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'PHP',
    note VARCHAR(255),
    transaction_status VARCHAR(1) NOT NULL DEFAULT '0',
    status VARCHAR(1) NOT NULL DEFAULT '1',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (user_id, full_name, user_name, email, phone_number, kyc_status, date_of_birth, address, status) VALUES
    ('USER001', 'User One', 'username1', 'user1@example.com', '09171234567', '1', '1990-01-01', 'Address 1', '1'),
    ('USER002', 'User Two', 'username2', 'user2@example.com', '09181234567', '0', '1991-02-02', 'Address 2', '0'),
    ('USER003', 'User Three', 'username3', 'user3@example.com', '09191234567', '1', '1992-03-03', 'Address 3', '1'),
    ('USER004', 'User Four', 'username4', 'user4@example.com', '09201234567', '0', '1993-04-04', 'Address 4', '0'),
    ('USER005', 'User Five', 'username5', 'user5@example.com', '09211234567', '1', '1994-05-05', 'Address 5', '1'),
    ('USER006', 'User Six', 'username6', 'user6@example.com', '09221234567', '0', '1995-06-06', 'Address 6', '0'),
    ('USER007', 'User Seven', 'username7', 'user7@example.com', '09231234567', '1', '1996-07-07', 'Address 7', '1'),
    ('USER008', 'User Eight', 'username8', 'user8@example.com', '09241234567', '0', '1997-08-08', 'Address 8', '0'),
    ('USER009', 'User Nine', 'username9', 'user9@example.com', '09251234567', '1', '1998-09-09', 'Address 9', '1'),
    ('USER010', 'User Ten', 'username10', 'user10@example.com', '09261234567', '0', '1999-10-10', 'Address 10', '0')
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO wallets (wallet_id, user_id, balance)
SELECT 'WALLET' || LPAD(ROW_NUMBER() OVER (ORDER BY user_id)::text, 3, '0'), user_id, 1000.00
FROM users
ON CONFLICT (user_id) DO NOTHING;

CREATE INDEX IF NOT EXISTS idx_transactions_sender ON transactions(sender_user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_recipient ON transactions(recipient_user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);
