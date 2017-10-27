package net.slipp.jhipster.web.rest;

import net.slipp.jhipster.App1App;

import net.slipp.jhipster.config.SecurityBeanOverrideConfiguration;

import net.slipp.jhipster.domain.Contents;
import net.slipp.jhipster.repository.ContentsRepository;
import net.slipp.jhipster.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContentsResource REST controller.
 *
 * @see ContentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App1App.class, SecurityBeanOverrideConfiguration.class})
public class ContentsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private ContentsRepository contentsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContentsMockMvc;

    private Contents contents;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContentsResource contentsResource = new ContentsResource(contentsRepository);
        this.restContentsMockMvc = MockMvcBuilders.standaloneSetup(contentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contents createEntity(EntityManager em) {
        Contents contents = new Contents()
            .title(DEFAULT_TITLE)
            .desc(DEFAULT_DESC);
        return contents;
    }

    @Before
    public void initTest() {
        contents = createEntity(em);
    }

    @Test
    @Transactional
    public void createContents() throws Exception {
        int databaseSizeBeforeCreate = contentsRepository.findAll().size();

        // Create the Contents
        restContentsMockMvc.perform(post("/api/contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contents)))
            .andExpect(status().isCreated());

        // Validate the Contents in the database
        List<Contents> contentsList = contentsRepository.findAll();
        assertThat(contentsList).hasSize(databaseSizeBeforeCreate + 1);
        Contents testContents = contentsList.get(contentsList.size() - 1);
        assertThat(testContents.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContents.getDesc()).isEqualTo(DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void createContentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentsRepository.findAll().size();

        // Create the Contents with an existing ID
        contents.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentsMockMvc.perform(post("/api/contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contents)))
            .andExpect(status().isBadRequest());

        // Validate the Contents in the database
        List<Contents> contentsList = contentsRepository.findAll();
        assertThat(contentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContents() throws Exception {
        // Initialize the database
        contentsRepository.saveAndFlush(contents);

        // Get all the contentsList
        restContentsMockMvc.perform(get("/api/contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contents.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void getContents() throws Exception {
        // Initialize the database
        contentsRepository.saveAndFlush(contents);

        // Get the contents
        restContentsMockMvc.perform(get("/api/contents/{id}", contents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contents.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContents() throws Exception {
        // Get the contents
        restContentsMockMvc.perform(get("/api/contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContents() throws Exception {
        // Initialize the database
        contentsRepository.saveAndFlush(contents);
        int databaseSizeBeforeUpdate = contentsRepository.findAll().size();

        // Update the contents
        Contents updatedContents = contentsRepository.findOne(contents.getId());
        updatedContents
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC);

        restContentsMockMvc.perform(put("/api/contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContents)))
            .andExpect(status().isOk());

        // Validate the Contents in the database
        List<Contents> contentsList = contentsRepository.findAll();
        assertThat(contentsList).hasSize(databaseSizeBeforeUpdate);
        Contents testContents = contentsList.get(contentsList.size() - 1);
        assertThat(testContents.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContents.getDesc()).isEqualTo(UPDATED_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingContents() throws Exception {
        int databaseSizeBeforeUpdate = contentsRepository.findAll().size();

        // Create the Contents

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContentsMockMvc.perform(put("/api/contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contents)))
            .andExpect(status().isCreated());

        // Validate the Contents in the database
        List<Contents> contentsList = contentsRepository.findAll();
        assertThat(contentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContents() throws Exception {
        // Initialize the database
        contentsRepository.saveAndFlush(contents);
        int databaseSizeBeforeDelete = contentsRepository.findAll().size();

        // Get the contents
        restContentsMockMvc.perform(delete("/api/contents/{id}", contents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contents> contentsList = contentsRepository.findAll();
        assertThat(contentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contents.class);
        Contents contents1 = new Contents();
        contents1.setId(1L);
        Contents contents2 = new Contents();
        contents2.setId(contents1.getId());
        assertThat(contents1).isEqualTo(contents2);
        contents2.setId(2L);
        assertThat(contents1).isNotEqualTo(contents2);
        contents1.setId(null);
        assertThat(contents1).isNotEqualTo(contents2);
    }
}
