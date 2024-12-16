package com.bank.kata.service;

import java.math.BigDecimal;

public interface AccountService {
    void debit(Long id, BigDecimal amount);
    void credit(Long id, BigDecimal amount);
}
