package com.example.leonproject.controller.service;

import com.example.leonproject.dao.entity.PunchClockDO;
import com.example.leonproject.dao.entity.TimeSheetDO;
import com.example.leonproject.dao.repository.PunchClockRepository;
import com.example.leonproject.dao.repository.TimeSheetRepository;
import com.example.leonproject.util.TimeDiffUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TimeSheetService {


    private final PunchClockRepository punchClockRepository;

    private final TimeSheetRepository timeSheetRepository;

    public TimeSheetService(PunchClockRepository punchClockRepository, TimeSheetRepository timeSheetRepository) {
        this.punchClockRepository = punchClockRepository;
        this.timeSheetRepository = timeSheetRepository;
    }

    public void createTimeSheetRecord(LocalDate date) {

        List<PunchClockDO> punchClockDOList = punchClockRepository.findAllUserWithCompleteAttendance(date);

        List<TimeSheetDO> timeSheetDOList = punchClockDOList.stream()
                .map(punchClockDO -> {
                    LocalDate workDate = punchClockDO.getClockIn().toLocalDate();
                    Time timeDiff = TimeDiffUtil.calculateTimeDiff(punchClockDO.getClockIn(), punchClockDO.getClockOut());
                    TimeSheetDO timeSheetDO = new TimeSheetDO();
                    timeSheetDO.setWorkDate(workDate);
                    timeSheetDO.setTimeDiff(timeDiff);
                    timeSheetDO.setAccountDO(punchClockDO.getAccountDO());
                    return timeSheetDO;
                })
                .collect(Collectors.toList());

        timeSheetRepository.saveAll(timeSheetDOList);

//        return timeSheetDOList;
    }
}
