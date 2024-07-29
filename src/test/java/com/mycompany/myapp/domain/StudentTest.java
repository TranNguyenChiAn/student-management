package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ClassesTestSamples.*;
import static com.mycompany.myapp.domain.CourseTestSamples.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;
import static com.mycompany.myapp.domain.TableTimeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void classesTest() {
        Student student = getStudentRandomSampleGenerator();
        Classes classesBack = getClassesRandomSampleGenerator();

        student.setClasses(classesBack);
        assertThat(student.getClasses()).isEqualTo(classesBack);

        student.classes(null);
        assertThat(student.getClasses()).isNull();
    }

    @Test
    void courseTest() {
        Student student = getStudentRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        student.setCourse(courseBack);
        assertThat(student.getCourse()).isEqualTo(courseBack);

        student.course(null);
        assertThat(student.getCourse()).isNull();
    }

    @Test
    void tableTimeTest() {
        Student student = getStudentRandomSampleGenerator();
        TableTime tableTimeBack = getTableTimeRandomSampleGenerator();

        student.setTableTime(tableTimeBack);
        assertThat(student.getTableTime()).isEqualTo(tableTimeBack);
        assertThat(tableTimeBack.getStudent()).isEqualTo(student);

        student.tableTime(null);
        assertThat(student.getTableTime()).isNull();
        assertThat(tableTimeBack.getStudent()).isNull();
    }
}
