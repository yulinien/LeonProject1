package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.PunchClockDO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application-test.properties")
public class PunchClockRepositoryTest {

    @Autowired
    private PunchClockRepository punchClockRepository;

    @Autowired
    private AccountRepository accountRepository;

    AccountDO accountDO1;

    AccountDO accountDO2;

    @BeforeEach
    void beforeEach() {

        accountDO1 = AccountDO.builder().username("testUser1").build();
        accountDO2 = AccountDO.builder().username("testUser2").build();
        accountDO1 = accountRepository.save(accountDO1);
        accountDO2 = accountRepository.save(accountDO2);

        List<PunchClockDO> punchClockDOList = Arrays.asList(

                PunchClockDO.builder().id(1)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 1, 10))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 1, 15))
                        .accountDO(accountDO1)
                        .build(),
                PunchClockDO.builder().id(2)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 1, 20))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 1, 25))
                        .accountDO(accountDO1)
                        .build(),
                PunchClockDO.builder().id(3)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 1, 40))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 1, 45))
                        .accountDO(accountDO2)
                        .build(),
                PunchClockDO.builder().id(4)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 1, 50))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 1, 55))
                        .accountDO(accountDO2)
                        .build(),
                PunchClockDO.builder().id(5)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 2, 50))
                        .accountDO(accountDO1)
                        .build(),
                PunchClockDO.builder().id(6)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 3, 50))
                        .accountDO(accountDO2)
                        .build(),
                PunchClockDO.builder().id(7)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 3, 50))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 3, 55))
                        .accountDO(accountDO1)
                        .build(),
                PunchClockDO.builder().id(8)
                        .clockIn(LocalDateTime.of(2022, 1, 1, 4, 50))
                        .clockOut(LocalDateTime.of(2022, 1, 1, 4, 55))
                        .accountDO(accountDO2)
                        .build()
        );

        punchClockRepository.saveAll(punchClockDOList);
    }

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @Test
    public void findLatestRecordsForEachUser_ReturnsAnUser() {

        Optional<PunchClockDO> result = punchClockRepository.findLatestRecordsForEachUser(accountDO1.getId());

        assertTrue(result.isPresent());
        assertEquals(7, result.get().getId());
    }

    @Test
    public void findAllUserWithCompleteAttendance_ReturnsAllUsers() {

        List<PunchClockDO> result = punchClockRepository.findAllUserWithCompleteAttendance(LocalDate.of(2022, 1, 1));

        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
    }
}
