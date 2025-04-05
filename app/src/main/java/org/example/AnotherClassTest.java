package org.example;

import org.example.annotation.ScheduleEveryFiveMins;

import java.util.Date;

public class AnotherClassTest {
    @ScheduleEveryFiveMins
    public void myJob() {
        System.out.println("AnotherClassTest: The current time is: " + new Date());
    }
}
