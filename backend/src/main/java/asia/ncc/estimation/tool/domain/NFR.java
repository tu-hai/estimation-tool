package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A NFR.
 */
@Entity
@Table(name = "nfr")
public class NFR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("nFRS")
    private ProjectType projectType;

    @Column(name = "standard")
    private String standard;

    @Column(name = "category")
    private String category;

    @Column(name = "applicable")
    private String applicable;

    @Column(name = "effort")
    private String effort;

    @Column(name = "comment")
    private String comment;

    @Column(name = "guidance")
    private String guidance;

    public String getCategory() {
        return category;
    }

    public NFR setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getStandard() {
        return standard;
    }

    public NFR setStandard(String standard) {
        this.standard = standard;
        return this;
    }

    public String getApplicable() {
        return applicable;
    }

    public NFR setApplicable(String applicable) {
        this.applicable = applicable;
        return this;
    }

    public String getEffort() {
        return effort;
    }

    public NFR setEffort(String effort) {
        this.effort = effort;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public NFR setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getGuidance() {
        return guidance;
    }

    public NFR setGuidance(String guidance) {
        this.guidance = guidance;
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

    public NFR name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public NFR projectType(ProjectType projectType) {
        this.projectType = projectType;
        return this;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
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
        NFR nFR = (NFR) o;
        if (nFR.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nFR.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NFR{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
