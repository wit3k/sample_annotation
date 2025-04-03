package com.sample;

import com.sample.annotation.ScheduleEveryFiveMins;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    @ScheduleEveryFiveMins
    public void myJob() {

        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("The current time is: " + new Date());
            }
        };
        Timer timer = new Timer("Timer");

        timer.scheduleAtFixedRate(task, 0, 500L);


    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        (new Main()).myJob();
        System.console().readLine();
    }
}