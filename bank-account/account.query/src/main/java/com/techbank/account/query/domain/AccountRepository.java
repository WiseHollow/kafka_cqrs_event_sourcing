package com.techbank.account.query.domain;

import com.techbank.cqrs.core.domain.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Bank Account Read Database
 */
public interface AccountRepository extends CrudRepository<BankAccount, String> {
    Optional<BankAccount> findByAccountHolder(String accountHolder);
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    List<BaseEntity> findByBalanceLessThan(double balance);
}
