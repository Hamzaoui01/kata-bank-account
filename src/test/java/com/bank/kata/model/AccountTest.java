package com.bank.kata.model;

import com.bank.kata.exception.InsufficientBalanceException;
import com.bank.kata.exception.InvalidAmountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    @DisplayName("Credit should fail and throws InvalidAmountException when amount<0 or null")
    void shouldNotCredit_whenAmountIsNegativeOrNull(){
        Account build = Account.builder().balance(BigDecimal.ZERO).build();
        BigDecimal negativeAmount = BigDecimal.valueOf(-1);
        Assertions.assertThrows(
                InvalidAmountException.class,
                ()-> build.credit(negativeAmount)
        );
        Assertions.assertThrows(
                InvalidAmountException.class,
                ()-> build.credit(null)
        );
    }

    @Test
    @DisplayName("Debit should fail and throws InvalidAmountException when amount<0 or null")
    void shouldNotDebit_whenAmountIsNegativeOrNull(){
        Account build = Account.builder().balance(BigDecimal.ZERO).build();
        BigDecimal negativeAmount = BigDecimal.valueOf(-1);
        Assertions.assertThrows(
                InvalidAmountException.class,
                ()-> build.debit(negativeAmount)
        );
        Assertions.assertThrows(
                InvalidAmountException.class,
                ()-> build.debit(null)
        );
    }

    @Test
    @DisplayName("Debit should add the amount to the current balance")
    void shouldAddAmount_whenDebit(){
        Account account = Account.builder().balance(BigDecimal.valueOf(200.00)).build();
        account.debit(BigDecimal.valueOf(20.99));
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(220.99));
    }

    @Test
    @DisplayName("Credit should reduce the amount from the current balance")
    void shouldReduceAmount_whenCredit(){
        Account account = Account.builder().balance(BigDecimal.valueOf(200.00)).build();
        account.credit(BigDecimal.valueOf(20.99));
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(179.01));
    }

    @Test
    @DisplayName("Credit should fail and throws InsufficientBalanceException when balance is not sufficient")
    void shouldNotCredit_whenBalanceLessThanAmount(){
        Account build = Account.builder().balance(BigDecimal.ZERO).build();
        Assertions.assertThrows(
                InsufficientBalanceException.class,
                ()-> build.credit(BigDecimal.ONE)
        );
    }

}
