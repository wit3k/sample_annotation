package org.example;

import org.example.annotation.ScheduleEveryFiveMins;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void sampleSchedule(long seconds) {
        TimerTask task = new TimerTask() {
            public void run() {
                (new Main()).myJob();
            }
        };
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, 0, 1000L * seconds);
    }

    @ScheduleEveryFiveMins
    public void myJob() {
        System.out.println("The current time is: " + new Date());
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        sampleSchedule(60 * 5);

        System.console().readLine();
    }
}