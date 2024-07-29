import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/classes">
        <Translate contentKey="global.menu.entities.classes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student">
        <Translate contentKey="global.menu.entities.student" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/table-time">
        <Translate contentKey="global.menu.entities.tableTime" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
