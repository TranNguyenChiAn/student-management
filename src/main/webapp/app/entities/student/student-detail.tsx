import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">
          <Translate contentKey="studentManagementApplicationApp.student.detail.title">Student</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="studentManagementApplicationApp.student.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.fullName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="studentManagementApplicationApp.student.email">Email</Translate>
            </span>
          </dt>
          <dd>{studentEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="studentManagementApplicationApp.student.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{studentEntity.phoneNumber}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="studentManagementApplicationApp.student.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{studentEntity.gender}</dd>
          <dt>
            <Translate contentKey="studentManagementApplicationApp.student.classes">Classes</Translate>
          </dt>
          <dd>{studentEntity.classes ? studentEntity.classes.id : ''}</dd>
          <dt>
            <Translate contentKey="studentManagementApplicationApp.student.course">Course</Translate>
          </dt>
          <dd>{studentEntity.course ? studentEntity.course.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
