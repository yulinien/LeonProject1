package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.PunchClockDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PunchClockRepository extends JpaRepository<PunchClockDO, Integer> {

    @Query("SELECT p FROM PunchClockDO p WHERE p.id IN " +
            "(SELECT MAX(pc.id) FROM PunchClockDO pc WHERE pc.accountDO.id = :accountId)")
    Optional<PunchClockDO> findLatestRecordsForEachUser(@Param("accountId") Integer accountId);

    @Query("SELECT p FROM PunchClockDO p WHERE DATE(p.clockIn) = :date AND p.clockOut IS NOT NULL ORDER BY p.accountDO.id ")
    List<PunchClockDO> findAllUserWithCompleteAttendance(@Param("date") LocalDate date);

}
