package com.example.leonproject.dao.repository;


import com.example.leonproject.dao.entity.AccountDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountDO, Integer> {

    Boolean existsByUsername(String username);
    
    Optional<AccountDO> findAccountByUsername(String username);
}
