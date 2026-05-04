CREATE TABLE account (
    iban VARCHAR(34) PRIMARY KEY
);
COMMENT ON COLUMN account.iban IS 'IBAN of the account (ISO 13616). Max 34 chars.';

CREATE TABLE account_user (
    iban      VARCHAR(34) REFERENCES account (iban),
    user_code VARCHAR(10),
    PRIMARY KEY (iban, user_code)
);
COMMENT ON COLUMN account_user.iban      IS 'IBAN of the account (ISO 13616).';
COMMENT ON COLUMN account_user.user_code IS 'User identifier. Placeholder — actual format depends on the bank''s customer numbering system.';
CREATE INDEX idx_account_user_user_code ON account_user (user_code);

CREATE TABLE currency (
    code CHAR(3) PRIMARY KEY CHECK (code = UPPER(code))
);
COMMENT ON COLUMN currency.code IS 'ISO 4217 currency code, uppercase (e.g. EUR).';

CREATE TABLE account_currency_balance (
    iban          VARCHAR(34)    REFERENCES account (iban),
    currency_code CHAR(3)        REFERENCES currency (code),
    balance       NUMERIC(19, 6) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    PRIMARY KEY (iban, currency_code)
);
COMMENT ON COLUMN account_currency_balance.iban          IS 'IBAN of the account (ISO 13616).';
COMMENT ON COLUMN account_currency_balance.currency_code IS 'ISO 4217 currency code, uppercase (e.g. EUR).';
COMMENT ON COLUMN account_currency_balance.balance       IS 'Current balance. Always non-negative. Precision: 6 decimal places.';
CREATE INDEX idx_account_currency_balance_iban ON account_currency_balance (iban);

CREATE TABLE currency_exchange_rate (
    key  CHAR(6)        PRIMARY KEY CHECK (key = UPPER(key) AND LENGTH(TRIM(key)) = 6),
    rate NUMERIC(19, 6) NOT NULL CHECK (rate > 0)
);
COMMENT ON COLUMN currency_exchange_rate.key  IS 'Concatenated uppercase ISO 4217 pair, e.g. EURUSD means 1 EUR = rate USD.';
COMMENT ON COLUMN currency_exchange_rate.rate IS 'How many units of toCurrency equal 1 unit of fromCurrency. Always positive.';