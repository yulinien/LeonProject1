package com.example.leonproject.schedule;

import com.example.leonproject.controller.service.TimeSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeSheetScheduler {

    private final TimeSheetService timeSheetService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeSheetScheduler(TimeSheetService timeSheetService) {
        this.timeSheetService = timeSheetService;
    }

//    @Scheduled(fixedDelay = 2000)
//    public void testFixDelay1(){
//        timeSheetService.createTimeSheetRecord(LocalDate.now());
//       logger.info("===fixedDelay1:時間:{}", LocalDateTime.now().format(formatter));
//    }

//    @Scheduled(fixedDelay = 1000)
//    public void testFixDelay2(){
//        logger.info("===fixedDelay2:時間:{}", LocalDateTime.now().format(formatter));
//    }
}
