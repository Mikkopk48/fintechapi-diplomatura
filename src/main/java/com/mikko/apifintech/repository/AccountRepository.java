package com.mikko.apifintech.repository;

import com.mikko.apifintech.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}