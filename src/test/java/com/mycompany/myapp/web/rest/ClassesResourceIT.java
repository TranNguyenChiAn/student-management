package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ClassesAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classes;
import com.mycompany.myapp.repository.ClassesRepository;
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
 * Integration tests for the {@link ClassesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassesResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassesMockMvc;

    private Classes classes;

    private Classes insertedClasses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classes createEntity(EntityManager em) {
        Classes classes = new Classes().className(DEFAULT_CLASS_NAME);
        return classes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classes createUpdatedEntity(EntityManager em) {
        Classes classes = new Classes().className(UPDATED_CLASS_NAME);
        return classes;
    }

    @BeforeEach
    public void initTest() {
        classes = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedClasses != null) {
            classesRepository.delete(insertedClasses);
            insertedClasses = null;
        }
    }

    @Test
    @Transactional
    void createClasses() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Classes
        var returnedClasses = om.readValue(
            restClassesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Classes.class
        );

        // Validate the Classes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClassesUpdatableFieldsEquals(returnedClasses, getPersistedClasses(returnedClasses));

        insertedClasses = returnedClasses;
    }

    @Test
    @Transactional
    void createClassesWithExistingId() throws Exception {
        // Create the Classes with an existing ID
        classes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classes)))
            .andExpect(status().isBadRequest());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClassNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classes.setClassName(null);

        // Create the Classes, which fails.

        restClassesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClasses() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        // Get all the classesList
        restClassesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classes.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)));
    }

    @Test
    @Transactional
    void getClasses() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        // Get the classes
        restClassesMockMvc
            .perform(get(ENTITY_API_URL_ID, classes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classes.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingClasses() throws Exception {
        // Get the classes
        restClassesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClasses() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classes
        Classes updatedClasses = classesRepository.findById(classes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClasses are not directly saved in db
        em.detach(updatedClasses);
        updatedClasses.className(UPDATED_CLASS_NAME);

        restClassesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClasses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedClasses))
            )
            .andExpect(status().isOk());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassesToMatchAllProperties(updatedClasses);
    }

    @Test
    @Transactional
    void putNonExistingClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(put(ENTITY_API_URL_ID, classes.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classes)))
            .andExpect(status().isBadRequest());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassesWithPatch() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classes using partial update
        Classes partialUpdatedClasses = new Classes();
        partialUpdatedClasses.setId(classes.getId());

        restClassesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClasses))
            )
            .andExpect(status().isOk());

        // Validate the Classes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClasses, classes), getPersistedClasses(classes));
    }

    @Test
    @Transactional
    void fullUpdateClassesWithPatch() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classes using partial update
        Classes partialUpdatedClasses = new Classes();
        partialUpdatedClasses.setId(classes.getId());

        partialUpdatedClasses.className(UPDATED_CLASS_NAME);

        restClassesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClasses))
            )
            .andExpect(status().isOk());

        // Validate the Classes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassesUpdatableFieldsEquals(partialUpdatedClasses, getPersistedClasses(partialUpdatedClasses));
    }

    @Test
    @Transactional
    void patchNonExistingClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classes.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClasses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClasses() throws Exception {
        // Initialize the database
        insertedClasses = classesRepository.saveAndFlush(classes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classes
        restClassesMockMvc
            .perform(delete(ENTITY_API_URL_ID, classes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classesRepository.count();
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

    protected Classes getPersistedClasses(Classes classes) {
        return classesRepository.findById(classes.getId()).orElseThrow();
    }

    protected void assertPersistedClassesToMatchAllProperties(Classes expectedClasses) {
        assertClassesAllPropertiesEquals(expectedClasses, getPersistedClasses(expectedClasses));
    }

    protected void assertPersistedClassesToMatchUpdatableProperties(Classes expectedClasses) {
        assertClassesAllUpdatablePropertiesEquals(expectedClasses, getPersistedClasses(expectedClasses));
    }
}
