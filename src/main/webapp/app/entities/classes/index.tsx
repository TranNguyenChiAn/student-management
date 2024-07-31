import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Classes from './classes';
import ClassesDetail from './classes-detail';
import ClassesUpdate from './classes-update';
import ClassesDeleteDialog from './classes-delete-dialog';

const ClassesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Classes />} />
    <Route path="new" element={<ClassesUpdate />} />
    <Route path=":id">
      <Route index element={<ClassesDetail />} />
      <Route path="edit" element={<ClassesUpdate />} />
      <Route path="delete" element={<ClassesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClassesRoutes;
