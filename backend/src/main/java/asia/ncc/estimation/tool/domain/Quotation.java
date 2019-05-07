package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Quotation.
 */
@Entity
@Table(name = "quotation")
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "stat_date")
    private LocalDate statDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JsonIgnoreProperties("quotations")
    private Customer customer;

    @OneToOne
    @JsonIgnoreProperties("quotations")
    private Project projectID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public Quotation statDate(LocalDate statDate) {
        this.statDate = statDate;
        return this;
    }

    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Quotation endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Customer getCustomerId() {
        return customer;
    }

    public Quotation customerId(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomerId(Customer customer) {
        this.customer = customer;
    }

    public Project getProjectID() {
        return projectID;
    }

    public Quotation projectID(Project project) {
        this.projectID = project;
        return this;
    }

    public void setProjectID(Project project) {
        this.projectID = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quotation quotation = (Quotation) o;
        if (quotation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Quotation{" +
            "id=" + getId() +
            ", projectId='"  + "'" +
            ", statDate='" + getStatDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
