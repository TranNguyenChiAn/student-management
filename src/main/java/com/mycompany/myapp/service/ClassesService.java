package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Classes;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Classes}.
 */
public interface ClassesService {
    /**
     * Save a classes.
     *
     * @param classes the entity to save.
     * @return the persisted entity.
     */
    Classes save(Classes classes);

    /**
     * Updates a classes.
     *
     * @param classes the entity to update.
     * @return the persisted entity.
     */
    Classes update(Classes classes);

    /**
     * Partially updates a classes.
     *
     * @param classes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Classes> partialUpdate(Classes classes);

    /**
     * Get all the classes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Classes> findAll(Pageable pageable);

    /**
     * Get the "id" classes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Classes> findOne(Long id);

    /**
     * Delete the "id" classes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
