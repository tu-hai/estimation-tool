package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ExtracEffort.
 */
@Entity
@Table(name = "extrac_effort")
public class ExtracEffort implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "decription")
    private String decription;
    @Column(name ="percentEffort")
    private Long PercentEfforts;
    @ManyToMany(mappedBy = "etracEffortIds")
    @JsonIgnore
    private Set<Project> projectIds = new HashSet<>();

    public Long getPercentEfforts() {
        return PercentEfforts;
    }

    public void setPercentEfforts(Long percentEfforts) {
        PercentEfforts = percentEfforts;
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

    public ExtracEffort name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public ExtracEffort decription(String decription) {
        this.decription = decription;
        return this;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Set<Project> getProjectIds() {
        return projectIds;
    }

    public ExtracEffort projectIds(Set<Project> projects) {
        this.projectIds = projects;
        return this;
    }

    public ExtracEffort addProjectId(Project project) {
        this.projectIds.add(project);
        project.getEtracEffortIds().add(this);
        return this;
    }

    public ExtracEffort removeProjectId(Project project) {
        this.projectIds.remove(project);
        project.getEtracEffortIds().remove(this);
        return this;
    }

    public void setProjectIds(Set<Project> projects) {
        this.projectIds = projects;
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
        ExtracEffort extracEffort = (ExtracEffort) o;
        if (extracEffort.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), extracEffort.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExtracEffort{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", decription='" + getDecription() + "'" +
            "}";
    }
}
