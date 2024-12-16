package com.bank.kata.mapper;

public interface EntityMapper<D, E> {
    D toDto(E entity);
}
