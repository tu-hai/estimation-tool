package asia.ncc.estimation.tool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProjectType.
 */
@Entity
@Table(name = "project_type")
public class ProjectType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decription")
    private String decription;

    @Column(name = "technogory")
    private String technogory;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "projecTypeId")
    private Set<Project> projects = new HashSet<>();
    @OneToMany(mappedBy = "projectType")
    private Set<NFR> nFRS = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecription() {
        return decription;
    }

    public ProjectType decription(String decription) {
        this.decription = decription;
        return this;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getTechnogory() {
        return technogory;
    }

    public ProjectType technogory(String technogory) {
        this.technogory = technogory;
        return this;
    }

    public void setTechnogory(String technogory) {
        this.technogory = technogory;
    }

    public String getName() {
        return name;
    }

    public ProjectType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public ProjectType projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public ProjectType addProject(Project project) {
        this.projects.add(project);
        project.setProjecTypeId(this);
        return this;
    }

    public ProjectType removeProject(Project project) {
        this.projects.remove(project);
        project.setProjecTypeId(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<NFR> getNFRS() {
        return nFRS;
    }

    public ProjectType nFRS(Set<NFR> nFRS) {
        this.nFRS = nFRS;
        return this;
    }

    public ProjectType addNFR(NFR nFR) {
        this.nFRS.add(nFR);
        nFR.setProjectType(this);
        return this;
    }

    public ProjectType removeNFR(NFR nFR) {
        this.nFRS.remove(nFR);
        nFR.setProjectType(null);
        return this;
    }

    public void setNFRS(Set<NFR> nFRS) {
        this.nFRS = nFRS;
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
        ProjectType projectType = (ProjectType) o;
        if (projectType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectType{" +
            "id=" + getId() +
            ", decription='" + getDecription() + "'" +
            ", technogory='" + getTechnogory() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
