package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CourseTestSamples.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;
import static com.mycompany.myapp.domain.TableTimeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void studentTest() {
        Course course = getCourseRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        course.addStudent(studentBack);
        assertThat(course.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getCourse()).isEqualTo(course);

        course.removeStudent(studentBack);
        assertThat(course.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getCourse()).isNull();

        course.students(new HashSet<>(Set.of(studentBack)));
        assertThat(course.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getCourse()).isEqualTo(course);

        course.setStudents(new HashSet<>());
        assertThat(course.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getCourse()).isNull();
    }

    @Test
    void tableTimeTest() {
        Course course = getCourseRandomSampleGenerator();
        TableTime tableTimeBack = getTableTimeRandomSampleGenerator();

        course.setTableTime(tableTimeBack);
        assertThat(course.getTableTime()).isEqualTo(tableTimeBack);
        assertThat(tableTimeBack.getCourse()).isEqualTo(course);

        course.tableTime(null);
        assertThat(course.getTableTime()).isNull();
        assertThat(tableTimeBack.getCourse()).isNull();
    }
}
