package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.PunchClockDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PunchClockRepository extends JpaRepository<PunchClockDO, Integer> {

    @Query("SELECT p FROM PunchClockDO p WHERE p.id IN " +
            "(SELECT MAX(pc.id) FROM PunchClockDO pc WHERE pc.accountDO.username = p.accountDO.username GROUP BY pc.accountDO.username)")
    Optional<PunchClockDO> findLatestRecordsForEachUser(String username);


}
