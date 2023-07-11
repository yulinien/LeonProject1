package com.example.leonproject.async;

import com.example.leonproject.dao.entity.PunchClockDO;
import com.example.leonproject.dao.repository.PunchClockRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class TimeSheetAsyncTask {

    private final TimeSheetAsyncService timeSheetAsyncService;
    private final ThreadPoolExecutor executor;

    public TimeSheetAsyncTask(PunchClockRepository punchClockRepository, TimeSheetAsyncService timeSheetAsyncService) {
        this.timeSheetAsyncService = timeSheetAsyncService;
        this.executor = new ThreadPoolExecutor(8, 8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    public boolean timeSheetAsync(LocalDate date) {
        List<PunchClockDO> punchClockDOList = timeSheetAsyncService.findAllUserWithCompleteAttendance(date);

        if (punchClockDOList.isEmpty()) {
            return false;
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (PunchClockDO punchClockDO : punchClockDOList) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                timeSheetAsyncService.createTimeSheetRecord(punchClockDO);
            }, executor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return true;
    }

}
