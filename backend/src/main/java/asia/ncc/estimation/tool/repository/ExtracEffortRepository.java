package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.ExtracEffort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExtracEffort entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtracEffortRepository extends JpaRepository<ExtracEffort, Long> {

}
