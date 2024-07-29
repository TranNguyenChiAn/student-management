package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ClassesTestSamples.*;
import static com.mycompany.myapp.domain.CourseTestSamples.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;
import static com.mycompany.myapp.domain.TableTimeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TableTimeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TableTime.class);
        TableTime tableTime1 = getTableTimeSample1();
        TableTime tableTime2 = new TableTime();
        assertThat(tableTime1).isNotEqualTo(tableTime2);

        tableTime2.setId(tableTime1.getId());
        assertThat(tableTime1).isEqualTo(tableTime2);

        tableTime2 = getTableTimeSample2();
        assertThat(tableTime1).isNotEqualTo(tableTime2);
    }

    @Test
    void classesTest() {
        TableTime tableTime = getTableTimeRandomSampleGenerator();
        Classes classesBack = getClassesRandomSampleGenerator();

        tableTime.setClasses(classesBack);
        assertThat(tableTime.getClasses()).isEqualTo(classesBack);

        tableTime.classes(null);
        assertThat(tableTime.getClasses()).isNull();
    }

    @Test
    void studentTest() {
        TableTime tableTime = getTableTimeRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        tableTime.setStudent(studentBack);
        assertThat(tableTime.getStudent()).isEqualTo(studentBack);

        tableTime.student(null);
        assertThat(tableTime.getStudent()).isNull();
    }

    @Test
    void courseTest() {
        TableTime tableTime = getTableTimeRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        tableTime.setCourse(courseBack);
        assertThat(tableTime.getCourse()).isEqualTo(courseBack);

        tableTime.course(null);
        assertThat(tableTime.getCourse()).isNull();
    }
}
