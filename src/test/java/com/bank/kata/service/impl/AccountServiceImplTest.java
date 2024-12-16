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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.bank.kata.enums.OperationType.CREDIT;
import static com.bank.kata.enums.OperationType.DEBIT;
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

    @ParameterizedTest()
    @CsvSource({"DEBIT","CREDIT"})
    @DisplayName("Handle Operation on account fails when account does not exist")
    void ShouldThrowException_whenDebitNonExistingAccount(String typeOp){
        OperationType type = OperationType.valueOf(typeOp);
        Long accountId = 1L;
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.handleOperation(accountId, type ,BigDecimal.ONE)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Account not found by id "+accountId));
    }

    @Test
    @DisplayName("When type operation is DEBIT, it should invoke debit on account entity, and save it")
    void shouldInvokeAccountDebit_andSaveAccount_whenDebit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.handleOperation(1L,DEBIT, amount);

        verify(mockAccount,times(1)).debit(amount);
        verify(accountRepository,times(1)).save(mockAccount);
    }

    @Test
    @DisplayName("When type operation is DEBIT,it should create DEBIT operation")
    void shouldCreateDebitOperation_whenDebit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any())).thenReturn(mockAccount);

        accountService.handleOperation(1L,DEBIT, amount);

        verify(operationService,times(1)).createOperation(mockAccount, OperationType.DEBIT,amount);
    }

    @Test
    @DisplayName("When type operation is CREDIT,it should invoke credit on account entity, and save it")
    void shouldInvokeAccountCredit_andSaveAccount_whenCredit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        accountService.handleOperation(1L,CREDIT, amount);

        verify(mockAccount,times(1)).credit(amount);
        verify(accountRepository,times(1)).save(mockAccount);
    }

    @Test
    @DisplayName("When type operation is CREDIT,it should create CREDIT operation")
    void shouldCreateCreditOperation_whenCredit(){
        Account mockAccount = mock(Account.class);
        BigDecimal amount = BigDecimal.valueOf(20.99);
        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any())).thenReturn(mockAccount);

        accountService.handleOperation(1L,CREDIT, amount);

        verify(operationService,times(1)).createOperation(mockAccount, CREDIT,amount);
    }

    @Test
    @DisplayName("When type operation is null,it should fail")
    void shouldFail_withException_whenNullOperationType() {
        String exceptionMsg = "Operation Type must not be null";
        BigDecimal amount = BigDecimal.valueOf(20);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.handleOperation(1L,null, amount)
        );
        Assertions.assertTrue(thrown.getMessage().contains(exceptionMsg));
    }


}
