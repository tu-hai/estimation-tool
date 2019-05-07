package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.ProjectType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProjectType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {

}
