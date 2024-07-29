import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TableTime from './table-time';
import TableTimeDetail from './table-time-detail';
import TableTimeUpdate from './table-time-update';
import TableTimeDeleteDialog from './table-time-delete-dialog';

const TableTimeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TableTime />} />
    <Route path="new" element={<TableTimeUpdate />} />
    <Route path=":id">
      <Route index element={<TableTimeDetail />} />
      <Route path="edit" element={<TableTimeUpdate />} />
      <Route path="delete" element={<TableTimeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TableTimeRoutes;
