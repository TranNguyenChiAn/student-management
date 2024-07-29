package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ClassesTestSamples.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;
import static com.mycompany.myapp.domain.TableTimeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClassesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classes.class);
        Classes classes1 = getClassesSample1();
        Classes classes2 = new Classes();
        assertThat(classes1).isNotEqualTo(classes2);

        classes2.setId(classes1.getId());
        assertThat(classes1).isEqualTo(classes2);

        classes2 = getClassesSample2();
        assertThat(classes1).isNotEqualTo(classes2);
    }

    @Test
    void studentTest() {
        Classes classes = getClassesRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        classes.addStudent(studentBack);
        assertThat(classes.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getClasses()).isEqualTo(classes);

        classes.removeStudent(studentBack);
        assertThat(classes.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getClasses()).isNull();

        classes.students(new HashSet<>(Set.of(studentBack)));
        assertThat(classes.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getClasses()).isEqualTo(classes);

        classes.setStudents(new HashSet<>());
        assertThat(classes.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getClasses()).isNull();
    }

    @Test
    void tableTimeTest() {
        Classes classes = getClassesRandomSampleGenerator();
        TableTime tableTimeBack = getTableTimeRandomSampleGenerator();

        classes.setTableTime(tableTimeBack);
        assertThat(classes.getTableTime()).isEqualTo(tableTimeBack);
        assertThat(tableTimeBack.getClasses()).isEqualTo(classes);

        classes.tableTime(null);
        assertThat(classes.getTableTime()).isNull();
        assertThat(tableTimeBack.getClasses()).isNull();
    }
}
