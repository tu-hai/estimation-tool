package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Quotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    @Query(value = "select distinct q from Quotation q")
    List<Quotation> findAllWithEagerRelationships();

    @Query(value = "select distinct q from Quotation q",
        countQuery = "select count(distinct q) from Quotation q")
    Page<Quotation> findAllWithEagerRelationships(Pageable pageable);
}
