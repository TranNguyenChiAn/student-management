package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TableTimeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TableTime getTableTimeSample1() {
        return new TableTime().id(1L);
    }

    public static TableTime getTableTimeSample2() {
        return new TableTime().id(2L);
    }

    public static TableTime getTableTimeRandomSampleGenerator() {
        return new TableTime().id(longCount.incrementAndGet());
    }
}
