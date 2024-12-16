package com.bank.kata.model;

import com.bank.kata.exception.InsufficientBalanceException;
import com.bank.kata.exception.InvalidAmountException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Debits the specified amount to the account. This method encapsulates the debits logic
     * within the entity itself, adhering to the principles of Domain-Driven Design (DDD).
     *
     * @param amount the amount to be credited; must be non-null and positive
     * @throws InvalidAmountException if the amount is null or non-positive
     */
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO)<0) throw new InvalidAmountException();
        balance = balance.add(amount);
    }

    /**
     * Credits the specified amount to the account. This method encapsulates the credit logic
     * within the entity itself, adhering to the principles of Domain-Driven Design (DDD).
     *
     * @param amount the amount to be credited; must be non-null and positive
     * @throws InvalidAmountException if the amount is null or non-positive
     * @throws InsufficientBalanceException if the amount exceed the current balance
     */
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO)<0) throw new InvalidAmountException();
        if(balance.compareTo(amount)<0) throw new InsufficientBalanceException();
        balance = balance.subtract(amount);
    }
}
