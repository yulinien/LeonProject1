package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.PunchClockDTO;
import com.example.leonproject.controller.pojo.PunchClockResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.entity.PunchClockDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.dao.repository.PunchClockRepository;
import com.example.leonproject.exception.InputValidationException;
import com.example.leonproject.util.TimeDiffUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PunchClockService {

    private final AccountRepository accountRepository;

    private final PunchClockRepository punchClockRepository;

    public PunchClockService(PunchClockRepository punchClockRepository, AccountRepository accountRepository) {
        this.punchClockRepository = punchClockRepository;
        this.accountRepository = accountRepository;
    }

    public PunchClockResponseDTO punchClock(PunchClockDTO punchClockDTO) {

        AccountDO accountDO = accountRepository.findAccountByUsername(punchClockDTO.getUsername())
                .orElseThrow(() -> new InputValidationException("Account not found"));

        PunchClockDO punchClockDO = punchClockRepository.findLatestRecordsForEachUser(punchClockDTO.getUsername())
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

}
