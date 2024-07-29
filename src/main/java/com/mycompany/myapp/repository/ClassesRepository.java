package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Classes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Classes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {}
