package com.bank.kata.service.impl;


import com.bank.kata.enums.OperationType;
import com.bank.kata.exception.AccountNotFoundException;
import com.bank.kata.model.Account;
import com.bank.kata.repository.AccountRepository;
import com.bank.kata.service.OperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    AccountServiceImpl accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    OperationService operationService;

    /*
    * Test of Balance change not implemented here as it's not a responsibility of this service,
    * instead the test verify invoking of responsible methods from Account Entity, which is
    * tested regarding the logic of updating balance.
     */

    @Test
    @DisplayName("Debit account fails when account does not exist")
    void ShouldThrowException_whenDebitNonExistingAccount(){
        Long accountId = 1L;
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.debit(accountId, BigDecimal.ONE)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Account not found by id "+accountId));
    }


    @Test
    @DisplayName("Credit account fails when account does not exist")
    void ShouldThrowException_whenCreditNonExistingAccount(){
        Long accountId = 1L;
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.credit(accountId, BigDecimal.ONE)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Account not found by id "+accountId));
    }

    @Test
    @DisplayName("Debit account should invoke debit on account entity, and save it")
    void shouldInvokeAccountDebit_andSaveAccount_whenDebit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.debit(1L, amount);

        verify(mockAccount,times(1)).debit(amount);
        verify(accountRepository,times(1)).save(mockAccount);
    }

    @Test
    @DisplayName("Debit account should create DEBIT operation")
    void shouldCreateDebitOperation_whenDebit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.debit(1L, amount);

        verify(operationService,times(1)).createOperation(mockAccount, OperationType.DEBIT,amount);
    }

    @Test
    @DisplayName("Credit account should invoke debit on account entity, and save it")
    void shouldInvokeAccountCredit_andSaveAccount_whenCredit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.credit(1L, amount);

        verify(mockAccount,times(1)).credit(amount);
        verify(accountRepository,times(1)).save(mockAccount);
    }

    @Test
    @DisplayName("Credit account should create CREDIT operation")
    void shouldCreateCreditOperation_whenCredit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.credit(1L, amount);

        verify(operationService,times(1)).createOperation(mockAccount, OperationType.CREDIT,amount);
    }

}
