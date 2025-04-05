package com.sample.annotation;

import org.example.annotation.ScheduleEveryFiveMins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ScheduleEveryFiveMinsRuntimeProcessor {

    final private Set<ScheduleableMethod> schedulableMethods = new HashSet<>();

    public ScheduleEveryFiveMinsRuntimeProcessor() {
        this.collectAllAnnotatedMethods();
        this.scheduleTasks();
    }

    private void collectAllAnnotatedMethods() {
        try {
            Class[] classes = getClasses("org.example");
            for (Class klass : classes) {
                while (klass != Object.class) {
                    for (final Method method : klass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(ScheduleEveryFiveMins.class)) {
                            schedulableMethods.add(new ScheduleableMethod(klass, method));

                        }
                    }
                    klass = klass.getSuperclass();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void scheduleTasks() {
        Timer timer = new Timer("Timer");
        for (ScheduleableMethod scheduleableMethod : schedulableMethods) {
            TimerTask task = null;
            try {
                task = getTimerTask(scheduleableMethod.clazz, scheduleableMethod.method);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            timer.scheduleAtFixedRate(task, 0, 5000L); //5 seconds instead of 5 minutes for easier testing
        }
    }

    private TimerTask getTimerTask(Class klass, Method method) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> ctor = klass.getConstructor();
        Object object = ctor.newInstance();

        return new TimerTask() {
            public void run() {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    // borrowed from https://stackoverflow.com/a/6593661
    private Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    // borrowed from https://stackoverflow.com/a/6593661
    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private record ScheduleableMethod(Class clazz, Method method) {}
}
