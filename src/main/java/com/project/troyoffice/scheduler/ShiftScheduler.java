package com.project.troyoffice.scheduler;

import com.project.troyoffice.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShiftScheduler {

    private final ShiftService shiftService;

    // Cron: Jalankan setiap jam 01:00 AM
    @Scheduled(cron = "0 0 1 * * ?")
    public void runDailyScheduleGeneration() {
        log.info("Scheduler started: Generating future schedules...");

        LocalDate today = LocalDate.now();
        // Strategy: Rolling Window
        // Kita generate jadwal untuk H+1 sampai H+7
        // Kenapa? Agar jika ada perubahan pattern dadakan hari ini, besok sudah terupdate.
        // Dan user bisa melihat jadwal mereka seminggu ke depan di Apps.
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = today.plusDays(7);

        shiftService.generateSchedules(startDate, endDate);

        log.info("Scheduler finished.");
    }
}
