package com.bank.kata.repository;

import com.bank.kata.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Long> {
    Page<Operation> findByAccountId(Long accountId,Pageable pageable);
}
