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

import java.time.Duration;
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

    private AccountDO accountDO;

    @BeforeEach
    public void setup() {
        accountDO = new AccountDO();
        accountDO.setId(1);
        accountDO.setUsername("testUser");

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

        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
        when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.empty());

        PunchClockResponseDTO response = punchClockService.punchClock(punchClockDTO);
        assertEquals("testUser", response.getUsername());
        assertEquals("打卡成功", response.getResponse());
    }

    @Test
    public void punchClock_PreviousPunchClockNotToday_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {

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

        when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
        when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.empty());

        PunchClockResponseDTO response = punchClockService.failPunchClock(punchClockDTO);
        assertEquals("testUser", response.getUsername());
        assertEquals("打卡成功", response.getResponse());
    }

    @Test
    public void failPunchClock_PreviousPunchClockNotToday_CreatesNewPunchClock() {
        try (MockedStatic<TimeDiffUtil> mocked = Mockito.mockStatic(TimeDiffUtil.class)) {

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
    public void failPunchClock_PunchClockDurationShorterThan50Sec_ThrowsException() {

        LocalDateTime fixedNow = LocalDateTime.of(2023, 7, 19, 12, 10, 0);
        LocalDateTime clockOutTime = fixedNow.minusSeconds(45);

        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class);
             MockedStatic<TimeDiffUtil> timeMockedStatic = Mockito.mockStatic(TimeDiffUtil.class);
             MockedStatic<Duration> durationMockedStatic = Mockito.mockStatic(Duration.class)) {

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockOut(clockOutTime);

            PunchClockDTO punchClockDTO = new PunchClockDTO();
            punchClockDTO.setUsername("testUser");

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);

            timeMockedStatic.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(false);
            Duration mockedDuration = Mockito.mock(Duration.class);
            durationMockedStatic.when(() -> Duration.between(clockOutTime, fixedNow)).thenReturn(mockedDuration);
            Mockito.when(mockedDuration.toSeconds()).thenReturn(45L);

            assertThrows(PunchClockFailException.class, () -> punchClockService.failPunchClock(punchClockDTO));
        }
    }

    @Test
    public void failPunchClock_PunchClockDurationLongerThan50Sec_CreatesNewPunchClock() {

        LocalDateTime fixedNow = LocalDateTime.of(2023, 7, 19, 12, 10, 0);
        LocalDateTime clockOutTime = fixedNow.minusSeconds(45);

        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class);
             MockedStatic<TimeDiffUtil> timeMockedStatic = Mockito.mockStatic(TimeDiffUtil.class);
             MockedStatic<Duration> durationMockedStatic = Mockito.mockStatic(Duration.class)) {

            PunchClockDO punchClockDO = new PunchClockDO();
            punchClockDO.setClockOut(clockOutTime);

            PunchClockDTO punchClockDTO = new PunchClockDTO();
            punchClockDTO.setUsername("testUser");

            when(accountRepository.findAccountByUsername(punchClockDTO.getUsername())).thenReturn(Optional.of(accountDO));
            when(punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())).thenReturn(Optional.of(punchClockDO));
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);

            timeMockedStatic.when(() -> TimeDiffUtil.isSameDate(punchClockDO.getClockIn())).thenReturn(false);
            Duration mockedDuration = Mockito.mock(Duration.class);
            durationMockedStatic.when(() -> Duration.between(clockOutTime, fixedNow)).thenReturn(mockedDuration);
            Mockito.when(mockedDuration.toSeconds()).thenReturn(100L);

            PunchClockResponseDTO response = punchClockService.failPunchClock(punchClockDTO);
            assertEquals("testUser", response.getUsername());
            assertEquals("打卡成功", response.getResponse());
        }
    }
}

/* TODO: failPunchClock的測試方法有些疑問 還有幾個方法 在模擬的時候出現小問題 */
