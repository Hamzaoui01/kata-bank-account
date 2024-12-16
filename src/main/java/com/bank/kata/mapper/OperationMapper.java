package com.bank.kata.mapper;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.model.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityMapper<OperationDto, Operation>{

    @Mapping(source = "balanceAfterOp",target = "balance")
    OperationDto toDto(Operation operation);

}