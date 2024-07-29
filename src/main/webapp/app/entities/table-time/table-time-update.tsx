import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClasses } from 'app/shared/model/classes.model';
import { getEntities as getClasses } from 'app/entities/classes/classes.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { ITableTime } from 'app/shared/model/table-time.model';
import { getEntity, updateEntity, createEntity, reset } from './table-time.reducer';

export const TableTimeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const classes = useAppSelector(state => state.classes.entities);
  const students = useAppSelector(state => state.student.entities);
  const courses = useAppSelector(state => state.course.entities);
  const tableTimeEntity = useAppSelector(state => state.tableTime.entity);
  const loading = useAppSelector(state => state.tableTime.loading);
  const updating = useAppSelector(state => state.tableTime.updating);
  const updateSuccess = useAppSelector(state => state.tableTime.updateSuccess);

  const handleClose = () => {
    navigate('/table-time');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getClasses({}));
    dispatch(getStudents({}));
    dispatch(getCourses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...tableTimeEntity,
      ...values,
      classes: classes.find(it => it.id.toString() === values.classes?.toString()),
      student: students.find(it => it.id.toString() === values.student?.toString()),
      course: courses.find(it => it.id.toString() === values.course?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...tableTimeEntity,
          classes: tableTimeEntity?.classes?.id,
          student: tableTimeEntity?.student?.id,
          course: tableTimeEntity?.course?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentManagementApplicationApp.tableTime.home.createOrEditLabel" data-cy="TableTimeCreateUpdateHeading">
            <Translate contentKey="studentManagementApplicationApp.tableTime.home.createOrEditLabel">Create or edit a TableTime</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="table-time-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="table-time-classes"
                name="classes"
                data-cy="classes"
                label={translate('studentManagementApplicationApp.tableTime.classes')}
                type="select"
              >
                <option value="" key="0" />
                {classes
                  ? classes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="table-time-student"
                name="student"
                data-cy="student"
                label={translate('studentManagementApplicationApp.tableTime.student')}
                type="select"
              >
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="table-time-course"
                name="course"
                data-cy="course"
                label={translate('studentManagementApplicationApp.tableTime.course')}
                type="select"
              >
                <option value="" key="0" />
                {courses
                  ? courses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/table-time" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TableTimeUpdate;
