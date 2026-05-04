INSERT INTO currency (code) VALUES ('EUR');
INSERT INTO currency (code) VALUES ('USD');
INSERT INTO currency (code) VALUES ('SEK');
INSERT INTO currency (code) VALUES ('GBP');


INSERT INTO currency_exchange_rate (key, rate) VALUES ('EURUSD', 1.085000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('USDEUR', 0.921659);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('EURGBP', 0.855000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('GBPEUR', 1.169590);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('EURSEK', 11.450000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('SEKEUR', 0.087336);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('USDGBP', 0.788000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('GBPUSD', 1.269035);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('USDSEK', 10.550000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('SEKUSD', 0.094787);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('GBPSEK', 13.400000);
INSERT INTO currency_exchange_rate (key, rate) VALUES ('SEKGBP', 0.074627);


INSERT INTO account (iban) VALUES ('EE471000001020145685');
INSERT INTO account (iban) VALUES ('EE382200221020145685');


INSERT INTO account_user (iban, user_code) VALUES ('EE471000001020145685', 'USER0001');
INSERT INTO account_user (iban, user_code) VALUES ('EE382200221020145685', 'USER0002');


INSERT INTO account_currency_balance (iban, currency_code, balance)
VALUES ('EE471000001020145685', 'EUR', 1500.500000);


INSERT INTO account_currency_balance (iban, currency_code, balance)
VALUES ('EE471000001020145685', 'USD', 250.000000);


INSERT INTO account_currency_balance (iban, currency_code, balance)
VALUES ('EE382200221020145685', 'EUR', 0.000000);