package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Assumption.
 */
@Entity
@Table(name = "assumption")
public class Assumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties("assumptions")
    private WorkItem workItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Assumption content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public Assumption note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public WorkItem getWorkItem() {
        return workItem;
    }

    public Assumption workItem(WorkItem workItem) {
        this.workItem = workItem;
        return this;
    }

    public void setWorkItem(WorkItem workItem) {
        this.workItem = workItem;
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
        Assumption assumption = (Assumption) o;
        if (assumption.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assumption.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Assumption{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
