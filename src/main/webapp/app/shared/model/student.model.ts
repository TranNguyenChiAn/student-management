import { IClasses } from 'app/shared/model/classes.model';
import { ICourse } from 'app/shared/model/course.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IStudent {
  id?: number;
  fullName?: string | null;
  email?: string | null;
  gender?: keyof typeof Gender | null;
  classes?: IClasses | null;
  course?: ICourse | null;
}

export const defaultValue: Readonly<IStudent> = {};
