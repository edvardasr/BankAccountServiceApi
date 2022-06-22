package com.forex.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.forex.entity.Account;

public interface ForexRepository extends JpaRepository<Account, Integer> {
	Account findByAccountnum(String accountnum);
}
