package com.provectus.cardiff;

import com.provectus.cardiff.entities.Person;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

/**
 * Created by artemvlasov on 10/10/15.
 */
public class App {
    public static void main(String[] args) {
        System.out.println(Period.between(LocalDate.now(), LocalDate.now().minusDays(10)).getDays());
    }
}
