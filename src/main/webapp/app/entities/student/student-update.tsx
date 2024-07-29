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
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { getEntity, updateEntity, createEntity, reset } from './student.reducer';

export const StudentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const classes = useAppSelector(state => state.classes.entities);
  const courses = useAppSelector(state => state.course.entities);
  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);
  const updating = useAppSelector(state => state.student.updating);
  const updateSuccess = useAppSelector(state => state.student.updateSuccess);
  const genderValues = Object.keys(Gender);

  const handleClose = () => {
    navigate('/student' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClasses({}));
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
      ...studentEntity,
      ...values,
      classes: classes.find(it => it.id.toString() === values.classes?.toString()),
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
          gender: 'MALE',
          ...studentEntity,
          classes: studentEntity?.classes?.id,
          course: studentEntity?.course?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentManagementApplicationApp.student.home.createOrEditLabel" data-cy="StudentCreateUpdateHeading">
            <Translate contentKey="studentManagementApplicationApp.student.home.createOrEditLabel">Create or edit a Student</Translate>
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
                  id="student-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentManagementApplicationApp.student.fullName')}
                id="student-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
              />
              <ValidatedField
                label={translate('studentManagementApplicationApp.student.email')}
                id="student-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('studentManagementApplicationApp.student.phoneNumber')}
                id="student-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('studentManagementApplicationApp.student.gender')}
                id="student-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {translate('studentManagementApplicationApp.Gender.' + gender)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="student-classes"
                name="classes"
                data-cy="classes"
                label={translate('studentManagementApplicationApp.student.classes')}
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
                id="student-course"
                name="course"
                data-cy="course"
                label={translate('studentManagementApplicationApp.student.course')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student" replace color="info">
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

export default StudentUpdate;
