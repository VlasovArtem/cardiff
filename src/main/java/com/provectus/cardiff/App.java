package com.provectus.cardiff;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by artemvlasov on 10/10/15.
 */
public class App {
    public static void main(String[] args) {
        LocalDate firstBookingStart = LocalDate.of(2015, Month.JANUARY, 1);
        LocalDate firstBookingEnd = LocalDate.of(2015, Month.JANUARY, 7);
        System.out.println(LocalDate.of(2015, Month.OCTOBER, 5).plusDays(7l));
    }
}
