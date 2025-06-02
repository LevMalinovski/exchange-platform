CREATE TABLE accounts
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP
);
CREATE TABLE assets
(
    id     UUID PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    name   VARCHAR(100),
    type   VARCHAR(20) NOT NULL
);
CREATE TABLE account_balances
(
    id         UUID PRIMARY KEY,
    account_id UUID    NOT NULL,
    asset_id   UUID    NOT NULL REFERENCES assets (id),
    amount     DECIMAL NOT NULL DEFAULT 0,
    UNIQUE (account_id, asset_id),
    created_at TIMESTAMP
);

CREATE INDEX idx_account_balances_user ON account_balances (account_id);
CREATE INDEX idx_account_balances_asset ON account_balances (asset_id);

CREATE TABLE balance_transactions
(
    id         UUID PRIMARY KEY,
    account_id UUID,
    asset_id   UUID,
    amount     DECIMAL,
    type       VARCHAR(20), -- 'DEPOSIT', 'WITHDRAW', 'BUY', 'SELL', 'CONVERT'
    reference  UUID,        -- optional: link to saga
    created_at TIMESTAMP
);
CREATE INDEX idx_asset_tx_user ON balance_transactions (account_id);
CREATE INDEX idx_asset_tx_asset ON balance_transactions (asset_id);
CREATE INDEX idx_asset_tx_type ON balance_transactions (type);
CREATE INDEX idx_asset_tx_created_at ON balance_transactions (created_at);

INSERT INTO assets (id, symbol, name, type)
VALUES (gen_random_uuid(), 'EUR', 'Euro', 'FIAT'),
       (gen_random_uuid(), 'BTC', 'Bitcoin', 'CRYPTO'),
       (gen_random_uuid(), 'ETH', 'Ethereum', 'CRYPTO'),
       (gen_random_uuid(), 'TSLA', 'Tesla Stock', 'STOCK'),
       (gen_random_uuid(), 'AAPL', 'Apple Stock', 'STOCK'),
       (gen_random_uuid(), 'GOLD', 'Gold Commodity', 'COMMODITY');

CREATE TABLE exchange_rates
(
    id         UUID PRIMARY KEY,
    asset_id   UUID,
    rate       DECIMAL NOT NULL,
    created_at TIMESTAMP
);

INSERT INTO exchange_rates (id, asset_id, rate, created_at)
VALUES (gen_random_uuid(),
        (SELECT id FROM assets WHERE symbol = 'EUR'),
        1.0,
        now());
CREATE INDEX idx_exchange_rate_asset ON exchange_rates (asset_id);

INSERT INTO accounts(id, email, created_at)
VALUES (gen_random_uuid(), 'test@test.org', NOW());

INSERT INTO account_balances (id, account_id, asset_id, amount, created_at)
VALUES (gen_random_uuid(),
        (SELECT id FROM accounts WHERE email = 'test@test.org'),
        (SELECT id FROM assets WHERE symbol = 'EUR'),
        0,
        now());

CREATE TABLE operations
(
    id            UUID PRIMARY KEY,
    type          VARCHAR(20) NOT NULL, -- DEPOSIT, WITHDRAW, BUY, SELL
    payload       JSONB       NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    failed_reason TEXT,
    retry_count   INT         NOT NULL DEFAULT 0,
    next_retry_at TIMESTAMP,
    created_at    TIMESTAMP            DEFAULT now(),
    updated_at    TIMESTAMP
);