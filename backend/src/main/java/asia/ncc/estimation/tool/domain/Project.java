


package asia.ncc.estimation.tool.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_strart")
    private LocalDate dateStrart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @ManyToOne
    private Size sizeId;

    @ManyToOne
    private Tech techId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wbs",
               joinColumns = @JoinColumn(name = "projects_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "work_item_ids_id", referencedColumnName = "id"))
    private Set<WorkItem> workItemIds = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_etrac_effort_id",
               joinColumns = @JoinColumn(name = "projects_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "etrac_effort_ids_id", referencedColumnName = "id"))
    private Set<ExtracEffort> etracEffortIds = new HashSet<>();

    @ManyToOne
    private ProjectType projecTypeId;

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

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateStrart() {
        return dateStrart;
    }

    public Project dateStrart(LocalDate dateStrart) {
        this.dateStrart = dateStrart;
        return this;
    }

    public void setDateStrart(LocalDate dateStrart) {
        this.dateStrart = dateStrart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public Project dateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Size getSizeId() {
        return sizeId;
    }

    public Project sizeId(Size size) {
        this.sizeId = size;
        return this;
    }

    public void setSizeId(Size size) {
        this.sizeId = size;
    }

    public Tech getTechId() {
        return techId;
    }

    public Project techId(Tech tech) {
        this.techId = tech;
        return this;
    }

    public void setTechId(Tech tech) {
        this.techId = tech;
    }

    public Set<WorkItem> getWorkItemIds() {
        return workItemIds;
    }

    public Project workItemIds(Set<WorkItem> workItems) {
        this.workItemIds = workItems;
        return this;
    }

    public Project addWorkItemId(WorkItem workItem) {
        this.workItemIds.add(workItem);
        workItem.getProjectIds().add(this);
        return this;
    }

    public Project removeWorkItemId(WorkItem workItem) {
        this.workItemIds.remove(workItem);
        workItem.getProjectIds().remove(this);
        return this;
    }

    public void setWorkItemIds(Set<WorkItem> workItems) {
        this.workItemIds = workItems;
    }

    public Set<ExtracEffort> getEtracEffortIds() {
        return etracEffortIds;
    }

    public Project etracEffortIds(Set<ExtracEffort> extracEfforts) {
        this.etracEffortIds = extracEfforts;
        return this;
    }

    public Project addEtracEffortId(ExtracEffort extracEffort) {
        this.etracEffortIds.add(extracEffort);
        extracEffort.getProjectIds().add(this);
        return this;
    }

    public Project removeEtracEffortId(ExtracEffort extracEffort) {
        this.etracEffortIds.remove(extracEffort);
        extracEffort.getProjectIds().remove(this);
        return this;
    }

    public void setEtracEffortIds(Set<ExtracEffort> extracEfforts) {
        this.etracEffortIds = extracEfforts;
    }

    public ProjectType getProjecTypeId() {
        return projecTypeId;
    }

    public Project projecTypeId(ProjectType projectType) {
        this.projecTypeId = projectType;
        return this;
    }

    public void setProjecTypeId(ProjectType projectType) {
        this.projecTypeId = projectType;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateStrart='" + getDateStrart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            "}";
    }
}
