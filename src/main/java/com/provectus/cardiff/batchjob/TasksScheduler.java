package com.provectus.cardiff.batchjob;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.service.CardBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Дмитрий on 29.09.2015.
 */
@Component
public class TasksScheduler {
    @Autowired
    private CardBookingService service;

    @Scheduled(cron = "0 0 0 * * ?")
    public void timer() {
        LocalDate dateTime = LocalDate.now();
        List<CardBooking> bookCardsList = service.getAll();
        for (int i = 0; i < bookCardsList.size(); i++) {
            if (dateTime.isAfter(bookCardsList.get(i).getBookingEndDate())) {
                service.deleteBookCardById(bookCardsList.get(i).getId());
            }
        }
    }
}
