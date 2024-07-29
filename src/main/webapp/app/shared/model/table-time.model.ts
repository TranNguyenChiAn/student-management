import { IClasses } from 'app/shared/model/classes.model';
import { IStudent } from 'app/shared/model/student.model';
import { ICourse } from 'app/shared/model/course.model';

export interface ITableTime {
  id?: number;
  classes?: IClasses | null;
  student?: IStudent | null;
  course?: ICourse | null;
}

export const defaultValue: Readonly<ITableTime> = {};
