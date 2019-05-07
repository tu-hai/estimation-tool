package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WorkItem.
 */
@Entity
@Table(name = "work_item")
public class WorkItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "actualeffort")
    private Long actualeffort;

    @Column(name = "taskname")
    private String taskname;

    @Column(name = "coding_effort")
    private Long codingEffort;

    @Column(name = "indates")
    private Integer indates;

    @OneToMany(mappedBy = "workItem")
    private Set<Assumption> assumptions = new HashSet<>();
    @ManyToMany(mappedBy = "workItemIds")
    @JsonIgnore
    private Set<Project> projectIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public WorkItem note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getActualeffort() {
        return actualeffort;
    }

    public WorkItem actualeffort(Long actualeffort) {
        this.actualeffort = actualeffort;
        return this;
    }

    public void setActualeffort(Long actualeffort) {
        this.actualeffort = actualeffort;
    }

    public String getTaskname() {
        return taskname;
    }

    public WorkItem taskname(String taskname) {
        this.taskname = taskname;
        return this;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public Long getCodingEffort() {
        return codingEffort;
    }

    public WorkItem codingEffort(Long codingEffort) {
        this.codingEffort = codingEffort;
        return this;
    }

    public void setCodingEffort(Long codingEffort) {
        this.codingEffort = codingEffort;
    }

    public Integer getIndates() {
        return indates;
    }

    public WorkItem indates(Integer indates) {
        this.indates = indates;
        return this;
    }

    public void setIndates(Integer indates) {
        this.indates = indates;
    }

    public Set<Assumption> getAssumptions() {
        return assumptions;
    }

    public WorkItem assumptions(Set<Assumption> assumptions) {
        this.assumptions = assumptions;
        return this;
    }

    public WorkItem addAssumption(Assumption assumption) {
        this.assumptions.add(assumption);
        assumption.setWorkItem(this);
        return this;
    }

    public WorkItem removeAssumption(Assumption assumption) {
        this.assumptions.remove(assumption);
        assumption.setWorkItem(null);
        return this;
    }

    public void setAssumptions(Set<Assumption> assumptions) {
        this.assumptions = assumptions;
    }

    public Set<Project> getProjectIds() {
        return projectIds;
    }

    public WorkItem projectIds(Set<Project> projects) {
        this.projectIds = projects;
        return this;
    }

    public WorkItem addProjectId(Project project) {
        this.projectIds.add(project);
        project.getWorkItemIds().add(this);
        return this;
    }

    public WorkItem removeProjectId(Project project) {
        this.projectIds.remove(project);
        project.getWorkItemIds().remove(this);
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
        WorkItem workItem = (WorkItem) o;
        if (workItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkItem{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", actualeffort='" + getActualeffort() + "'" +
            ", taskname='" + getTaskname() + "'" +
            ", codingEffort=" + getCodingEffort() +
            ", indates=" + getIndates() +
            "}";
    }
}
