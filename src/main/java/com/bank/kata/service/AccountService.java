package com.bank.kata.service;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.enums.OperationType;

import java.math.BigDecimal;

public interface AccountService {
    OperationDto handleOperation(Long accountId, OperationType type, BigDecimal amount);
}
