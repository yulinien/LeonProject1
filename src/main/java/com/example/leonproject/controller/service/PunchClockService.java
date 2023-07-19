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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
public class PunchClockService {

    private final AccountRepository accountRepository;

    private final PunchClockRepository punchClockRepository;

    public PunchClockService(PunchClockRepository punchClockRepository, AccountRepository accountRepository) {
        this.punchClockRepository = punchClockRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public PunchClockResponseDTO punchClock(PunchClockDTO punchClockDTO) {

        AccountDO accountDO = accountRepository.findAccountByUsername(punchClockDTO.getUsername())
                .orElseThrow(() -> new InputValidationException("Account not found"));

        PunchClockDO punchClockDO = punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())
                .orElse(null);

        //isPositiveTimeDiff
        if (punchClockDO == null || !(TimeDiffUtil.isSameDate(punchClockDO.getClockIn()))) {
            PunchClockDO newPunchClockDO = new PunchClockDO();
            newPunchClockDO.setClockIn(LocalDateTime.now());
            newPunchClockDO.setAccountDO(accountDO);
            punchClockRepository.save(newPunchClockDO);
        } else {
            punchClockDO.setClockOut(LocalDateTime.now());
            punchClockRepository.save(punchClockDO);
        }

        return new PunchClockResponseDTO(punchClockDTO.getUsername(), "打卡成功");
    }

    /**
     * 沒有transactional的示範
     **/
    public PunchClockResponseDTO failPunchClock(PunchClockDTO punchClockDTO) {

        AccountDO accountDO = accountRepository.findAccountByUsername(punchClockDTO.getUsername())
                .orElseThrow(() -> new InputValidationException("Account not found"));

        PunchClockDO punchClockDO = punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())
                .orElse(null);

        //當使用者打卡時間間隔過短就產生錯誤 但一樣要將紀錄寫進後端
        if (punchClockDO == null || !(TimeDiffUtil.isSameDate(punchClockDO.getClockIn()))) {
            PunchClockDO newPunchClockDO = new PunchClockDO();
            newPunchClockDO.setClockIn(LocalDateTime.now());
            newPunchClockDO.setAccountDO(accountDO);
            punchClockRepository.save(newPunchClockDO);
            log.info("好啊");
        }

        if (punchClockDO != null && Duration.between(punchClockDO.getClockOut(), LocalDateTime.now()).toSeconds() <= 50) {
            punchClockDO.setClockOut(LocalDateTime.now());
            punchClockRepository.save(punchClockDO);
            throw new PunchClockFailException(LocalTime.now() + "打卡時間間隔過短");
        }

        if (punchClockDO != null) {
            punchClockDO.setClockOut(LocalDateTime.now());
            punchClockRepository.save(punchClockDO);
        }

        return new PunchClockResponseDTO(punchClockDTO.getUsername(), "打卡成功");
    }

    /**
     * 加了transactional的示範
     **/
    @Transactional
    public PunchClockResponseDTO failPunchClockTransactional(PunchClockDTO punchClockDTO) {

        AccountDO accountDO = accountRepository.findAccountByUsername(punchClockDTO.getUsername())
                .orElseThrow(() -> new InputValidationException("Account not found"));

        PunchClockDO punchClockDO = punchClockRepository.findLatestRecordsForEachUser(accountDO.getId())
                .orElse(null);

        //當使用者打卡時間間隔過短就產生錯誤 但一樣要將紀錄寫進後端
        if (punchClockDO == null || !(TimeDiffUtil.isSameDate(punchClockDO.getClockIn()))) {
            PunchClockDO newPunchClockDO = new PunchClockDO();
            newPunchClockDO.setClockIn(LocalDateTime.now());
            newPunchClockDO.setAccountDO(accountDO);
            punchClockRepository.save(newPunchClockDO);
        }

        if (punchClockDO != null && Duration.between(punchClockDO.getClockOut(), LocalDateTime.now()).toSeconds() <= 50) {
            punchClockDO.setClockOut(LocalDateTime.now());
            punchClockRepository.save(punchClockDO);
            throw new PunchClockFailException(LocalTime.now() + "打卡時間間隔過短");
        }

        if (punchClockDO != null) {
            punchClockDO.setClockOut(LocalDateTime.now());
            punchClockRepository.save(punchClockDO);
        }

        return new PunchClockResponseDTO(punchClockDTO.getUsername(), "打卡成功");
    }
}
