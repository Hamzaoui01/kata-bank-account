package com.bank.kata.service.impl;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;

    /**
     *
     * Manage the debit operation on an account, it updates the account and saves the operation
     *
     * @param accountId the account id
     * @param amount the amount to debit
     */
    @Transactional
    public void debit(Long accountId,BigDecimal amount) {
        log.debug("Processing debit for account {}: amount={}", accountId, amount);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        account.debit(amount);
        accountRepository.save(account);
        operationService.createOperation(account, OperationType.DEBIT,amount);
        log.info("Successfully debited account {}: amount={}, new balance={}", accountId, amount, account.getBalance());
    }

    /**
     * Manage the credit transaction on account, it updates the account and saves the operation
     * @param accountId the account id
     * @param amount the amount to withdraw
     */
    @Transactional
    public void credit(Long accountId,BigDecimal amount) {
        log.info("Start credit account {}",accountId);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        account.credit(amount);
        accountRepository.save(account);
        operationService.createOperation(account, OperationType.CREDIT,amount);
        log.info("End credit account {}",accountId);
    }


}
