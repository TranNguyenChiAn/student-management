import course from 'app/entities/course/course.reducer';
import classes from 'app/entities/classes/classes.reducer';
import student from 'app/entities/student/student.reducer';
import tableTime from 'app/entities/table-time/table-time.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  course,
  classes,
  student,
  tableTime,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
