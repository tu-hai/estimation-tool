package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.Size;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Size entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

}
