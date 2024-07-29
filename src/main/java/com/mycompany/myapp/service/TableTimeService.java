package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TableTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.TableTime}.
 */
public interface TableTimeService {
    /**
     * Save a tableTime.
     *
     * @param tableTime the entity to save.
     * @return the persisted entity.
     */
    TableTime save(TableTime tableTime);

    /**
     * Updates a tableTime.
     *
     * @param tableTime the entity to update.
     * @return the persisted entity.
     */
    TableTime update(TableTime tableTime);

    /**
     * Partially updates a tableTime.
     *
     * @param tableTime the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TableTime> partialUpdate(TableTime tableTime);

    /**
     * Get all the tableTimes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TableTime> findAll(Pageable pageable);

    /**
     * Get the "id" tableTime.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TableTime> findOne(Long id);

    /**
     * Delete the "id" tableTime.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
