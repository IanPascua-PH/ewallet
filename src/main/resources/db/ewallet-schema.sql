CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    kyc_status VARCHAR(20) NOT NULL,
    date_of_birth DATE,
    address VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_id VARCHAR(20) NOT NULL UNIQUE,
    user_id VARCHAR(20) NOT NULL UNIQUE REFERENCES users(user_id),
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'PHP',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT balance_non_negative CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(20) NOT NULL UNIQUE,
    sender_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
    recipient_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT amount_positive CHECK (amount > 0),
    CONSTRAINT different_users CHECK (sender_id != recipient_id)
);

-- Insert seed data (users)
INSERT INTO users (user_id, full_name, email, phone_number, kyc_status, date_of_birth, address, status) VALUES
    ('USER001', 'User One', 'user1@example.com', '09171234567', 'verified', '1990-01-01', 'Address 1', 'active'),
    ('USER002', 'User Two', 'user2@example.com', '09181234567', 'verified', '1991-02-02', 'Address 2', 'active'),
    ('USER003', 'User Three', 'user3@example.com', '09191234567', 'unverified', '1992-03-03', 'Address 3', 'inactive'),
    ('USER004', 'User Four', 'user4@example.com', '09201234567', 'verified', '1993-04-04', 'Address 4', 'active'),
    ('USER005', 'User Five', 'user5@example.com', '09211234567', 'unverified', '1994-05-05', 'Address 5', 'inactive'),
    ('USER006', 'User Six', 'user6@example.com', '09221234567', 'verified', '1995-06-06', 'Address 6', 'active'),
    ('USER007', 'User Seven', 'user7@example.com', '09231234567', 'unverified', '1996-07-07', 'Address 7', 'inactive'),
    ('USER008', 'User Eight', 'user8@example.com', '09241234567', 'verified', '1997-08-08', 'Address 8', 'active'),
    ('USER009', 'User Nine', 'user9@example.com', '09251234567', 'unverified', '1998-09-09', 'Address 9', 'inactive'),
    ('USER010', 'User Ten', 'user10@example.com', '09261234567', 'verified', '1999-10-10', 'Address 10', 'active')
ON CONFLICT (user_id) DO NOTHING;

-- Insert wallets for users (after users exist)
INSERT INTO wallets (wallet_id, user_id, balance)
SELECT 'WALLET' || LPAD(ROW_NUMBER() OVER (ORDER BY user_id)::text, 3, '0'), user_id, 1000.00
FROM users
ON CONFLICT (user_id) DO NOTHING;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_transactions_sender ON transactions(sender_id);
CREATE INDEX IF NOT EXISTS idx_transactions_recipient ON transactions(recipient_id);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);
