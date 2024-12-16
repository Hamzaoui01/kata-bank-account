package com.bank.kata.service.impl;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.enums.OperationType;
import com.bank.kata.exception.AccountNotFoundException;
import com.bank.kata.model.Account;
import com.bank.kata.repository.AccountRepository;
import com.bank.kata.service.AccountService;
import com.bank.kata.service.OperationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;

    /**
     * Handle the possible operation on account
     * @param accountId Account ID
     * @param type Type of operation
     * @param amount amount of operation
     * @return Operation created is returned
     */
    @Transactional
    @Override
    public OperationDto handleOperation(Long accountId, OperationType type, BigDecimal amount) {
        log.info("[START] processing {} request on account {}",type,accountId);
        if (Objects.isNull(type)){
            throw new IllegalArgumentException("Operation Type must not be null");
        }
        Account account = updateAccount(accountId, type, amount);
        OperationDto operation = operationService.createOperation(account, type, amount);
        log.info("[END] processing {} request on account {}",type,accountId);
        return operation;
    }

    private Account updateAccount(Long accountId, OperationType type, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        switch (type){
            case CREDIT -> account.credit(amount);
            case DEBIT -> account.debit(amount);
            default -> throw new IllegalArgumentException("Unhandled Operation Type: "+ type);
        }
        return accountRepository.save(account);
    }


}
