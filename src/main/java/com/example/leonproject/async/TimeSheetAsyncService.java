package com.example.leonproject.async;

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

@Transactional
@Service
public class TimeSheetAsyncService {

    private final PunchClockRepository punchClockRepository;

    private final TimeSheetRepository timeSheetRepository;

    public TimeSheetAsyncService(PunchClockRepository punchClockRepository, TimeSheetRepository timeSheetRepository) {
        this.punchClockRepository = punchClockRepository;
        this.timeSheetRepository = timeSheetRepository;
    }

    public List<PunchClockDO> findAllUserWithCompleteAttendance(LocalDate date) {
        return punchClockRepository.findAllUserWithCompleteAttendance(date);
    }

    public void createTimeSheetRecord(PunchClockDO punchClockDO) {
        LocalDate workDate = punchClockDO.getClockIn().toLocalDate();
        Time timeDiff = TimeDiffUtil.calculateTimeDiff(punchClockDO.getClockIn(), punchClockDO.getClockOut());
        TimeSheetDO timeSheetDO = new TimeSheetDO();
        timeSheetDO.setWorkDate(workDate);
        timeSheetDO.setTimeDiff(timeDiff);
        timeSheetDO.setAccountDO(punchClockDO.getAccountDO());
        timeSheetRepository.save(timeSheetDO);
    }
}
