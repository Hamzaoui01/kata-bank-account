package com.bank.kata.service.impl;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.enums.OperationType;
import com.bank.kata.exception.OperationsNotFoundException;
import com.bank.kata.mapper.OperationMapper;
import com.bank.kata.model.Account;
import com.bank.kata.model.Operation;
import com.bank.kata.repository.OperationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceImplTest {

    @Mock
    OperationRepository operationRepository;

    @Mock
    OperationMapper operationMapper;

    @InjectMocks
    OperationServiceImpl operationService;

    @Test
    void shouldPersistOperation(){
        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.ZERO)
                .build();
        LocalDateTime localDateTime = LocalDateTime.now();
        operationService.createOperation(account, OperationType.DEBIT,BigDecimal.TEN);
        ArgumentCaptor<Operation> operationCapture = ArgumentCaptor.forClass(Operation.class);
        Mockito.verify(operationRepository,times(1)).save(operationCapture.capture());
        Operation operation = operationCapture.getValue();
        Assertions.assertThat(operation.getAmount()).isEqualByComparingTo(BigDecimal.TEN);
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.DEBIT);
        Assertions.assertThat(operation.getAccount()).isEqualTo(account);
        Assertions.assertThat(operation.getDateTime()).isAfterOrEqualTo(localDateTime);
        Assertions.assertThat(operation.getDateTime()).isBefore(LocalDateTime.now());
        Assertions.assertThat(operation.getBalanceAfterOp()).isEqualByComparingTo(account.getBalance());
    }

    @Test
    void shouldReturnOperationList(){
        Operation operation = Operation.builder().type(OperationType.DEBIT).amount(BigDecimal.TEN).dateTime(LocalDateTime.now()).build();
        when(operationRepository.findByAccountId(any(),any())).thenReturn(new PageImpl<>(List.of(operation)));
        Page<OperationDto> accountOperations = operationService.getAccountOperations(1L, PageRequest.of(2, 2));
        Assertions.assertThat(accountOperations.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldThrowException_whenNoOperationFound(){
        when(operationRepository.findByAccountId(any(),any())).thenReturn(new PageImpl<>(List.of()));
        PageRequest pageRequest = PageRequest.of(1, 2);
        OperationsNotFoundException thrown = assertThrows(
                OperationsNotFoundException.class,
                () -> operationService.getAccountOperations(1L, pageRequest)
        );
        Assertions.assertThat(thrown.getMessage()).isEqualTo("No Operations found for requested account");
    }
}
