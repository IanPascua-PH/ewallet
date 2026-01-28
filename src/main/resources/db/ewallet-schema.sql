CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL,
    user_id VARCHAR(20) PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wallets (
    id BIGSERIAL,
    wallet_id VARCHAR(20) PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL UNIQUE REFERENCES users(user_id),
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT balance_non_negative CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL,
    transaction_id VARCHAR(20) PRIMARY KEY,
    sender_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
    recipient_id VARCHAR(20) NOT NULL REFERENCES users(user_id),
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT amount_positive CHECK (amount > 0),
    CONSTRAINT different_users CHECK (sender_id != recipient_id)
);

INSERT INTO wallets (user_id, balance)
SELECT user_id, 1000.00 FROM users
ON CONFLICT (user_id) DO NOTHING;

-- Create indexes for better query performance
CREATE INDEX idx_transactions_sender ON transactions(sender_id);
CREATE INDEX idx_transactions_recipient ON transactions(recipient_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Insert seed data (users with IDs matching jsonplaceholder.typicode.com)
INSERT INTO users (user_id) VALUES
    ('USER001'), ('USER002'), ('USER003'), ('USER004'), ('USER005'),
    ('USER006'), ('USER007'), ('USER008'), ('USER009'), ('USER010')
ON CONFLICT (user_id) DO NOTHING;
