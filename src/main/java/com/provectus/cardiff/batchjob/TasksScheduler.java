package com.provectus.cardiff.batchjob;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.persistence.repository.CardBookingRepository;
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
    private CardBookingRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void expiredCardBookingScheduler() {
        List<CardBooking> bookCardsList = repository.findByBookingEndDateAfter(LocalDate.now());
        if(!bookCardsList.isEmpty()) {
            bookCardsList.stream().peek(cb -> repository.delete(cb.getId()));
        }
    }
}
