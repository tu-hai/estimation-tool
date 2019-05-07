package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.Assumption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Assumption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssumptionRepository extends JpaRepository<Assumption, Long> {

}

