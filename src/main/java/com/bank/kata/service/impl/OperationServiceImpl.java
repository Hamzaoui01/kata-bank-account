package com.bank.kata.service.impl;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.enums.OperationType;
import com.bank.kata.exception.OperationsNotFoundException;
import com.bank.kata.mapper.OperationMapper;
import com.bank.kata.model.Account;
import com.bank.kata.model.Operation;
import com.bank.kata.repository.OperationRepository;
import com.bank.kata.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Override
    public Page<OperationDto> getAccountOperations(Long accountId, PageRequest pageRequest) {
        log.info("Get Operations for account {}",accountId);
        Page<OperationDto> map = operationRepository.findByAccountId(accountId, pageRequest).map(operationMapper::toDto);
        if (map.getTotalElements()==0) throw new OperationsNotFoundException();
        return map;
    }

    @Override
    public OperationDto createOperation(Account account, OperationType type, BigDecimal amount) {
        Operation operation = operationRepository.save(Operation.builder()
                .account(account)
                .amount(amount)
                .type(type)
                .balanceAfterOp(account.getBalance())
                .dateTime(LocalDateTime.now())
                .build());
        log.info(" {} Operation created for account {}",type, account.getId());
        return operationMapper.toDto(operation);
    }
}
