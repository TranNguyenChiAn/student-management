package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.TableTimeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TableTime;
import com.mycompany.myapp.repository.TableTimeRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TableTimeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TableTimeResourceIT {

    private static final String ENTITY_API_URL = "/api/table-times";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TableTimeRepository tableTimeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTableTimeMockMvc;

    private TableTime tableTime;

    private TableTime insertedTableTime;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TableTime createEntity(EntityManager em) {
        TableTime tableTime = new TableTime();
        return tableTime;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TableTime createUpdatedEntity(EntityManager em) {
        TableTime tableTime = new TableTime();
        return tableTime;
    }

    @BeforeEach
    public void initTest() {
        tableTime = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTableTime != null) {
            tableTimeRepository.delete(insertedTableTime);
            insertedTableTime = null;
        }
    }

    @Test
    @Transactional
    void createTableTime() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TableTime
        var returnedTableTime = om.readValue(
            restTableTimeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tableTime)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TableTime.class
        );

        // Validate the TableTime in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTableTimeUpdatableFieldsEquals(returnedTableTime, getPersistedTableTime(returnedTableTime));

        insertedTableTime = returnedTableTime;
    }

    @Test
    @Transactional
    void createTableTimeWithExistingId() throws Exception {
        // Create the TableTime with an existing ID
        tableTime.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTableTimeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tableTime)))
            .andExpect(status().isBadRequest());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTableTimes() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        // Get all the tableTimeList
        restTableTimeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tableTime.getId().intValue())));
    }

    @Test
    @Transactional
    void getTableTime() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        // Get the tableTime
        restTableTimeMockMvc
            .perform(get(ENTITY_API_URL_ID, tableTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tableTime.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTableTime() throws Exception {
        // Get the tableTime
        restTableTimeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTableTime() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tableTime
        TableTime updatedTableTime = tableTimeRepository.findById(tableTime.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTableTime are not directly saved in db
        em.detach(updatedTableTime);

        restTableTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTableTime.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTableTime))
            )
            .andExpect(status().isOk());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTableTimeToMatchAllProperties(updatedTableTime);
    }

    @Test
    @Transactional
    void putNonExistingTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tableTime.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tableTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tableTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tableTime)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTableTimeWithPatch() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tableTime using partial update
        TableTime partialUpdatedTableTime = new TableTime();
        partialUpdatedTableTime.setId(tableTime.getId());

        restTableTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTableTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTableTime))
            )
            .andExpect(status().isOk());

        // Validate the TableTime in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTableTimeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTableTime, tableTime),
            getPersistedTableTime(tableTime)
        );
    }

    @Test
    @Transactional
    void fullUpdateTableTimeWithPatch() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tableTime using partial update
        TableTime partialUpdatedTableTime = new TableTime();
        partialUpdatedTableTime.setId(tableTime.getId());

        restTableTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTableTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTableTime))
            )
            .andExpect(status().isOk());

        // Validate the TableTime in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTableTimeUpdatableFieldsEquals(partialUpdatedTableTime, getPersistedTableTime(partialUpdatedTableTime));
    }

    @Test
    @Transactional
    void patchNonExistingTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tableTime.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tableTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tableTime))
            )
            .andExpect(status().isBadRequest());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTableTime() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tableTime.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableTimeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tableTime)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TableTime in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTableTime() throws Exception {
        // Initialize the database
        insertedTableTime = tableTimeRepository.saveAndFlush(tableTime);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tableTime
        restTableTimeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tableTime.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tableTimeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TableTime getPersistedTableTime(TableTime tableTime) {
        return tableTimeRepository.findById(tableTime.getId()).orElseThrow();
    }

    protected void assertPersistedTableTimeToMatchAllProperties(TableTime expectedTableTime) {
        assertTableTimeAllPropertiesEquals(expectedTableTime, getPersistedTableTime(expectedTableTime));
    }

    protected void assertPersistedTableTimeToMatchUpdatableProperties(TableTime expectedTableTime) {
        assertTableTimeAllUpdatablePropertiesEquals(expectedTableTime, getPersistedTableTime(expectedTableTime));
    }
}
