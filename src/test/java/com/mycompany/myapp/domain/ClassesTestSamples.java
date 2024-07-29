package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClassesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Classes getClassesSample1() {
        return new Classes().id(1L).className("className1");
    }

    public static Classes getClassesSample2() {
        return new Classes().id(2L).className("className2");
    }

    public static Classes getClassesRandomSampleGenerator() {
        return new Classes().id(longCount.incrementAndGet()).className(UUID.randomUUID().toString());
    }
}
