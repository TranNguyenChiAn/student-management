package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Course}.
 */
public interface CourseService {
    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    Course save(Course course);

    /**
     * Updates a course.
     *
     * @param course the entity to update.
     * @return the persisted entity.
     */
    Course update(Course course);

    /**
     * Partially updates a course.
     *
     * @param course the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Course> partialUpdate(Course course);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Course> findAll(Pageable pageable);

    /**
     * Get all the Course where TableTime is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Course> findAllWhereTableTimeIsNull();

    /**
     * Get the "id" course.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Course> findOne(Long id);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}