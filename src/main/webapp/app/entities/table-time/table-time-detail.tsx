import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './table-time.reducer';

export const TableTimeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tableTimeEntity = useAppSelector(state => state.tableTime.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tableTimeDetailsHeading">
          <Translate contentKey="studentManagementApplicationApp.tableTime.detail.title">TableTime</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tableTimeEntity.id}</dd>
          <dt>
            <Translate contentKey="studentManagementApplicationApp.tableTime.classes">Classes</Translate>
          </dt>
          <dd>{tableTimeEntity.classes ? tableTimeEntity.classes.id : ''}</dd>
          <dt>
            <Translate contentKey="studentManagementApplicationApp.tableTime.student">Student</Translate>
          </dt>
          <dd>{tableTimeEntity.student ? tableTimeEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="studentManagementApplicationApp.tableTime.course">Course</Translate>
          </dt>
          <dd>{tableTimeEntity.course ? tableTimeEntity.course.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/table-time" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/table-time/${tableTimeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TableTimeDetail;
