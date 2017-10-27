package net.slipp.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.slipp.jhipster.domain.Contents;

import net.slipp.jhipster.repository.ContentsRepository;
import net.slipp.jhipster.web.rest.errors.BadRequestAlertException;
import net.slipp.jhipster.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contents.
 */
@RestController
@RequestMapping("/api")
public class ContentsResource {

    private final Logger log = LoggerFactory.getLogger(ContentsResource.class);

    private static final String ENTITY_NAME = "contents";

    private final ContentsRepository contentsRepository;

    public ContentsResource(ContentsRepository contentsRepository) {
        this.contentsRepository = contentsRepository;
    }

    /**
     * POST  /contents : Create a new contents.
     *
     * @param contents the contents to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contents, or with status 400 (Bad Request) if the contents has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contents")
    @Timed
    public ResponseEntity<Contents> createContents(@RequestBody Contents contents) throws URISyntaxException {
        log.debug("REST request to save Contents : {}", contents);
        if (contents.getId() != null) {
            throw new BadRequestAlertException("A new contents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contents result = contentsRepository.save(contents);
        return ResponseEntity.created(new URI("/api/contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contents : Updates an existing contents.
     *
     * @param contents the contents to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contents,
     * or with status 400 (Bad Request) if the contents is not valid,
     * or with status 500 (Internal Server Error) if the contents couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contents")
    @Timed
    public ResponseEntity<Contents> updateContents(@RequestBody Contents contents) throws URISyntaxException {
        log.debug("REST request to update Contents : {}", contents);
        if (contents.getId() == null) {
            return createContents(contents);
        }
        Contents result = contentsRepository.save(contents);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contents.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contents : get all the contents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contents in body
     */
    @GetMapping("/contents")
    @Timed
    public List<Contents> getAllContents() {
        log.debug("REST request to get all Contents");
        return contentsRepository.findAll();
        }

    /**
     * GET  /contents/:id : get the "id" contents.
     *
     * @param id the id of the contents to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contents, or with status 404 (Not Found)
     */
    @GetMapping("/contents/{id}")
    @Timed
    public ResponseEntity<Contents> getContents(@PathVariable Long id) {
        log.debug("REST request to get Contents : {}", id);
        Contents contents = contentsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contents));
    }

    /**
     * DELETE  /contents/:id : delete the "id" contents.
     *
     * @param id the id of the contents to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteContents(@PathVariable Long id) {
        log.debug("REST request to delete Contents : {}", id);
        contentsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
