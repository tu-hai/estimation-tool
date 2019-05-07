package asia.ncc.estimation.tool.repository;

import asia.ncc.estimation.tool.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "select distinct customer from Customer customer",
        countQuery = "select count(distinct customer) from Customer customer")
    Page<Customer> findAllWithEagerRelationships(Pageable pageable);
}
