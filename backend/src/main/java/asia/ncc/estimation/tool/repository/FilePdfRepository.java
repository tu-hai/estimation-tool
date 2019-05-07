package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.FilePdf;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FilePdf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilePdfRepository extends JpaRepository<FilePdf, Long> {

}
