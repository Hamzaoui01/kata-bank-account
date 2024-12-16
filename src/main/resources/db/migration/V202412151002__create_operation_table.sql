CREATE TABLE operation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    balance_after_Op DECIMAL(19, 2) NOT NULL,
    type VARCHAR(10) NOT NULL,
    creation_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id)
);