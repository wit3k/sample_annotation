package org.example;

import com.sample.annotation.ScheduleEveryFiveMinsRuntimeProcessor;
import org.example.annotation.ScheduleEveryFiveMins;

import java.util.Date;

public class Main {

    @ScheduleEveryFiveMins
    public void myJob() {
        System.out.println("The current time is: " + new Date());
    }

    @ScheduleEveryFiveMins
    public void myJob2() {
        System.out.println("myJob2: The current time is: " + new Date());
    }

    @ScheduleEveryFiveMins
    public void myJob3() {
        System.out.println("myJob3: The current time is: " + new Date());
    }

    public static void main(String[] args) {
        //we can think of ScheduleEveryFiveMinsRuntimeProcessor as a singleton service
        //it has to have one instance so we can collect all annotated methods at runtime and schedule tasks
        new ScheduleEveryFiveMinsRuntimeProcessor();

        System.console().readLine();
    }

}