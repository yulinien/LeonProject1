package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.PunchClockDTO;
import com.example.leonproject.controller.pojo.PunchClockResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.PunchClockDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.dao.repository.PunchClockRepository;
import com.example.leonproject.exception.InputValidationException;
import com.example.leonproject.exception.PunchClockFailException;
import com.example.leonproject.util.TimeDiffUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PunchClockServiceTest {

    @InjectMocks
    private PunchClockService punchClockService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PunchClockRepository punchClockRepository;

    private PunchClockDTO punchClockDTO;

    @BeforeEach
    public void setup() {
        punchClockDTO = new PunchClockDTO();
        punchClockDTO.setUsername("testUser");
    }

    @Test
    public void punchClock_UserNotFound_ThrowsException() {
        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.empty());
        assertThrows(InputValidationException.class, () -> punchClockService.punchClock(punchClockDTO));
    }

    @Test
    public void punchClock_NoPreviousPunchClock_CreatesNewPunchClock() {

        AccountDO accountDO = new AccountDO();
        accountDO.setId(1);
        accountDO.setUsername("testUser");

        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
        when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.empty());

        PunchClockResponseDTO response = punchClockService.punchClock(punchClockDTO);
        assertEquals("testUser", response.getUsername());
        assertEquals("打卡成功", response.getResponse());
    }

    @Test
    public void punchClock_PreviousPunchClockNotToday_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockIn(LocalDateTime.now().minusDays(1));

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            ;
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));

            mocked.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(false);

            PunchClockResponseDTO response = punchClockService.punchClock(punchClockDTO);
            assertEquals("testUser", response.getUsername());
            assertEquals("打卡成功", response.getResponse());

            verify(punchClockRepository).save(any(PunchClockDO.class));

        }
    }

    @Test
    public void punchClock_PreviousPunchClockIsToday_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockIn(LocalDateTime.now().minusDays(1));

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            ;
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));
            mocked.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(true);

            PunchClockResponseDTO response = punchClockService.punchClock(punchClockDTO);
            assertEquals("testUser", response.getUsername());
            assertEquals("打卡成功", response.getResponse());

            verify(punchClockRepository).save(any(PunchClockDO.class));

        }
    }

    @Test
    public void failPunchClock_UserNotFound_ThrowsException() {
        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.empty());
        assertThrows(InputValidationException.class, () -> punchClockService.punchClock(punchClockDTO));
    }

    @Test
    public void failPunchClock_NoPreviousPunchClock_CreatesNewPunchClock() {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(1);
        accountDO.setUsername("testUser");

        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
        when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.empty());

        PunchClockResponseDTO response = punchClockService.failPunchClock(punchClockDTO);
        assertEquals("testUser", response.getUsername());
        assertEquals("打卡成功", response.getResponse());
    }

    @Test
    public void failPunchClock_PreviousPunchClockNotToday_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockIn(LocalDateTime.now().minusDays(1));

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.empty());
            mocked.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(false);

            PunchClockResponseDTO response = punchClockService.failPunchClock(punchClockDTO);
            assertEquals("testUser", response.getUsername());
            assertEquals("打卡成功", response.getResponse());

            verify(punchClockRepository, times(1)).save(any(PunchClockDO.class));
        }
    }

    @Test
    public void failPunchClock_PunchClockDurationTooShort_ThrowsException() {
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {

            LocalDateTime fixedNow = LocalDateTime.of(2023, 7, 19, 12, 0);
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);

            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");

            PunchClockDO punchClockDO = new PunchClockDO();
            LocalDateTime clockOutTime = fixedNow.minusSeconds(45);  // 设置ClockOut时间使其与fixedNow之间的时间差为45秒
            punchClockDO.setClockOut(clockOutTime);

            PunchClockDTO punchClockDTO = new PunchClockDTO();
            punchClockDTO.setUsername("testUser");

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));

            assertThrows(PunchClockFailException.class, () -> punchClockService.failPunchClock(punchClockDTO));
        }
    }

    @Test
    public void failPunchClock_PunchClock_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {
            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockOut(LocalDateTime.of(2023, 7, 19, 12, 0));

            LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 19, 12, 0).minusSeconds(60);

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));

            mocked.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(true);

            try (MockedStatic<LocalDateTime> timeMocked = Mockito.mockStatic(LocalDateTime.class)) {

                timeMocked.when(LocalDateTime::now).thenReturn(localDateTime);

                PunchClockResponseDTO response = punchClockService.failPunchClock(punchClockDTO);
                assertEquals("testUser", response.getUsername());
                assertEquals("打卡成功", response.getResponse());

                verify(punchClockRepository, times(1)).save(any(PunchClockDO.class));

            }

        }
    }
}

/* TODO: failPunchClock的測試方法卡住 還有幾個方法需要完成 在引用靜態方法的時候出現問題 */
