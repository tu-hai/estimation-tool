package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.Tech;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Tech entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {

}
