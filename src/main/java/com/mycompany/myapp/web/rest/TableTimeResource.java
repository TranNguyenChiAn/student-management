package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TableTime;
import com.mycompany.myapp.repository.TableTimeRepository;
import com.mycompany.myapp.service.TableTimeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TableTime}.
 */
@RestController
@RequestMapping("/api/table-times")
public class TableTimeResource {

    private static final Logger log = LoggerFactory.getLogger(TableTimeResource.class);

    private static final String ENTITY_NAME = "tableTime";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableTimeService tableTimeService;

    private final TableTimeRepository tableTimeRepository;

    public TableTimeResource(TableTimeService tableTimeService, TableTimeRepository tableTimeRepository) {
        this.tableTimeService = tableTimeService;
        this.tableTimeRepository = tableTimeRepository;
    }

    /**
     * {@code POST  /table-times} : Create a new tableTime.
     *
     * @param tableTime the tableTime to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tableTime, or with status {@code 400 (Bad Request)} if the tableTime has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TableTime> createTableTime(@RequestBody TableTime tableTime) throws URISyntaxException {
        log.debug("REST request to save TableTime : {}", tableTime);
        if (tableTime.getId() != null) {
            throw new BadRequestAlertException("A new tableTime cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tableTime = tableTimeService.save(tableTime);
        return ResponseEntity.created(new URI("/api/table-times/" + tableTime.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tableTime.getId().toString()))
            .body(tableTime);
    }

    /**
     * {@code PUT  /table-times/:id} : Updates an existing tableTime.
     *
     * @param id the id of the tableTime to save.
     * @param tableTime the tableTime to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tableTime,
     * or with status {@code 400 (Bad Request)} if the tableTime is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tableTime couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TableTime> updateTableTime(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TableTime tableTime
    ) throws URISyntaxException {
        log.debug("REST request to update TableTime : {}, {}", id, tableTime);
        if (tableTime.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableTime.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableTimeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tableTime = tableTimeService.update(tableTime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tableTime.getId().toString()))
            .body(tableTime);
    }

    /**
     * {@code PATCH  /table-times/:id} : Partial updates given fields of an existing tableTime, field will ignore if it is null
     *
     * @param id the id of the tableTime to save.
     * @param tableTime the tableTime to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tableTime,
     * or with status {@code 400 (Bad Request)} if the tableTime is not valid,
     * or with status {@code 404 (Not Found)} if the tableTime is not found,
     * or with status {@code 500 (Internal Server Error)} if the tableTime couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TableTime> partialUpdateTableTime(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TableTime tableTime
    ) throws URISyntaxException {
        log.debug("REST request to partial update TableTime partially : {}, {}", id, tableTime);
        if (tableTime.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableTime.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableTimeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TableTime> result = tableTimeService.partialUpdate(tableTime);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tableTime.getId().toString())
        );
    }

    /**
     * {@code GET  /table-times} : get all the tableTimes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tableTimes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TableTime>> getAllTableTimes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TableTimes");
        Page<TableTime> page = tableTimeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /table-times/:id} : get the "id" tableTime.
     *
     * @param id the id of the tableTime to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tableTime, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TableTime> getTableTime(@PathVariable("id") Long id) {
        log.debug("REST request to get TableTime : {}", id);
        Optional<TableTime> tableTime = tableTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tableTime);
    }

    /**
     * {@code DELETE  /table-times/:id} : delete the "id" tableTime.
     *
     * @param id the id of the tableTime to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTableTime(@PathVariable("id") Long id) {
        log.debug("REST request to delete TableTime : {}", id);
        tableTimeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
