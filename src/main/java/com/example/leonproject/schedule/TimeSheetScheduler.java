package com.example.leonproject.schedule;

import com.example.leonproject.async.TimeSheetAsyncService;
import com.example.leonproject.async.TimeSheetAsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeSheetScheduler {

    private final TimeSheetAsyncTask timeSheetAsyncTask;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeSheetScheduler(TimeSheetAsyncTask timeSheetAsyncTask) {
        this.timeSheetAsyncTask = timeSheetAsyncTask;
    }

    @Scheduled(fixedDelay = 20000)
    public void timeSheetSchedule() {
        boolean result = timeSheetAsyncTask.timeSheetAsync(LocalDate.now());
        if (result) {
            logger.info("時間:{}工時表更新成功", LocalDateTime.now().format(formatter));
        } else {
            logger.info("更新失敗");
        }
    }
}
