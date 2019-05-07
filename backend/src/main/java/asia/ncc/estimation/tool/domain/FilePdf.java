package asia.ncc.estimation.tool.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A FilePdf.
 */
@Entity
@Table(name = "file_pdf")
public class FilePdf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "id_customer")
    private Long idCustomer;

    @Column(name = "id_quotation")
    private Long idQuotation;

    public Long getIdQuotation() {
        return idQuotation;
    }

    public FilePdf setIdQuotation(Long idQuotation) {
        this.idQuotation = idQuotation;
        return this;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FilePdf name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public FilePdf location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public FilePdf dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public FilePdf idCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
        return this;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
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
        FilePdf filePdf = (FilePdf) o;
        if (filePdf.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), filePdf.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FilePdf{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", dateCreate='" + getDateCreate() + "'" +
            ", idCustomer=" + getIdCustomer() +
            "}";
    }
}
