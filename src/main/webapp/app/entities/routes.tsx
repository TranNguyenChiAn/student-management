import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Course from './course';
import Classes from './classes';
import Student from './student';
import TableTime from './table-time';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="course/*" element={<Course />} />
        <Route path="classes/*" element={<Classes />} />
        <Route path="student/*" element={<Student />} />
        <Route path="table-time/*" element={<TableTime />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
