package net.slipp.jhipster.repository;

import net.slipp.jhipster.domain.Contents;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Contents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {

}
