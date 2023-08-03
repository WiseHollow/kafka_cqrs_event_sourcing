package com.techbank.account.query.domain;

import com.techbank.account.common.dtos.AccountType;
import com.techbank.cqrs.core.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends BaseEntity {

    @Id
    private String id;
    private String accountHolder;
    private Date creationDate;
    private AccountType accountType;
    private double balance;
}
