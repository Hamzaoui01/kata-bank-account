package com.bank.kata.service;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.enums.OperationType;
import com.bank.kata.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

public interface OperationService {
    Page<OperationDto> getAccountOperations(Long accountId, PageRequest pageRequest);
    OperationDto createOperation(Account account, OperationType debit, BigDecimal amount);
}
