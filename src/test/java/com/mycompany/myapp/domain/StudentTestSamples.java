package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Student getStudentSample1() {
        return new Student().id(1L).fullName("fullName1").email("email1");
    }

    public static Student getStudentSample2() {
        return new Student().id(2L).fullName("fullName2").email("email2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student().id(longCount.incrementAndGet()).fullName(UUID.randomUUID().toString()).email(UUID.randomUUID().toString());
    }
}
