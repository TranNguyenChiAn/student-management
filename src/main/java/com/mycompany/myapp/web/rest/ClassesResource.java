package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Classes;
import com.mycompany.myapp.repository.ClassesRepository;
import com.mycompany.myapp.service.ClassesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Classes}.
 */
@RestController
@RequestMapping("/api/classes")
public class ClassesResource {

    private static final Logger log = LoggerFactory.getLogger(ClassesResource.class);

    private static final String ENTITY_NAME = "classes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassesService classesService;

    private final ClassesRepository classesRepository;

    public ClassesResource(ClassesService classesService, ClassesRepository classesRepository) {
        this.classesService = classesService;
        this.classesRepository = classesRepository;
    }

    /**
     * {@code POST  /classes} : Create a new classes.
     *
     * @param classes the classes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classes, or with status {@code 400 (Bad Request)} if the classes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Classes> createClasses(@Valid @RequestBody Classes classes) throws URISyntaxException {
        log.debug("REST request to save Classes : {}", classes);
        if (classes.getId() != null) {
            throw new BadRequestAlertException("A new classes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        classes = classesService.save(classes);
        return ResponseEntity.created(new URI("/api/classes/" + classes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, classes.getId().toString()))
            .body(classes);
    }

    /**
     * {@code PUT  /classes/:id} : Updates an existing classes.
     *
     * @param id the id of the classes to save.
     * @param classes the classes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classes,
     * or with status {@code 400 (Bad Request)} if the classes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Classes> updateClasses(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Classes classes
    ) throws URISyntaxException {
        log.debug("REST request to update Classes : {}, {}", id, classes);
        if (classes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        classes = classesService.update(classes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classes.getId().toString()))
            .body(classes);
    }

    /**
     * {@code PATCH  /classes/:id} : Partial updates given fields of an existing classes, field will ignore if it is null
     *
     * @param id the id of the classes to save.
     * @param classes the classes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classes,
     * or with status {@code 400 (Bad Request)} if the classes is not valid,
     * or with status {@code 404 (Not Found)} if the classes is not found,
     * or with status {@code 500 (Internal Server Error)} if the classes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Classes> partialUpdateClasses(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Classes classes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classes partially : {}, {}", id, classes);
        if (classes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Classes> result = classesService.partialUpdate(classes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classes.getId().toString())
        );
    }

    /**
     * {@code GET  /classes} : get all the classes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Classes>> getAllClasses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Classes");
        Page<Classes> page = classesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classes/:id} : get the "id" classes.
     *
     * @param id the id of the classes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Classes> getClasses(@PathVariable("id") Long id) {
        log.debug("REST request to get Classes : {}", id);
        Optional<Classes> classes = classesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classes);
    }

    @GetMapping("/available")
    public List<Classes> getAvailableClassrooms() {
        List<Classes> allClassrooms = classesService.getAvailableClass();
        return ResponseEntity.ok(allClassrooms).getBody();
    }

    /**
     * {@code DELETE  /classes/:id} : delete the "id" classes.
     *
     * @param id the id of the classes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasses(@PathVariable("id") Long id) {
        log.debug("REST request to delete Classes : {}", id);
        classesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
