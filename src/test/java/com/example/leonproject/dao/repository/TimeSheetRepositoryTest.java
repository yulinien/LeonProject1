package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.TimeSheetDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application-test.properties")
public class TimeSheetRepositoryTest {

    @Autowired
    TimeSheetRepository timeSheetRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void setTimeSheetRepository_SaveAll_ReturnsSuccess() {

        for (int i = 1; i <= 10; i++) {
            TimeSheetDO timeSheetDO = new TimeSheetDO();
            timeSheetDO.setId(i);
            timeSheetDO.setWorkDate(LocalDate.now());
            timeSheetDO.setTimeDiff(Time.valueOf(LocalTime.of(i, 1)));

            timeSheetRepository.save(timeSheetDO);
        }

        List<TimeSheetDO> savedTimeSheetList = timeSheetRepository.findAll();
        Assertions.assertEquals(10, savedTimeSheetList.size());
    }


}
