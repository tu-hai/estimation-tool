package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.NFR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NFR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NFRRepository extends JpaRepository<NFR, Long> {
    @Query(value = "select distinct nfr from NFR nfr")
    Page<NFR> findNFRs(Pageable pageable);
}
