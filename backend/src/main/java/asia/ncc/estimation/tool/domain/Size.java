package asia.ncc.estimation.tool.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Size.
 */
@Entity
@Table(name = "size")
public class Size implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_size")
    private String nameSize;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSize() {
        return nameSize;
    }

    public Size nameSize(String nameSize) {
        this.nameSize = nameSize;
        return this;
    }

    public void setNameSize(String nameSize) {
        this.nameSize = nameSize;
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
        Size size = (Size) o;
        if (size.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), size.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Size{" +
            "id=" + getId() +
            ", nameSize='" + getNameSize() + "'" +
            "}";
    }
}
