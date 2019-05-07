package asia.ncc.estimation.tool.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Tech.
 */
@Entity
@Table(name = "tech")
public class Tech implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "tech_contact")
    private String techContact;

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

    public Tech name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Tech description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechContact() {
        return techContact;
    }

    public Tech techContact(String techContact) {
        this.techContact = techContact;
        return this;
    }

    public void setTechContact(String techContact) {
        this.techContact = techContact;
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
        Tech tech = (Tech) o;
        if (tech.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tech.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tech{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", techContact='" + getTechContact() + "'" +
            "}";
    }
}
