package asia.ncc.estimation.tool.service.dto;

import asia.ncc.estimation.tool.domain.NFR;
import asia.ncc.estimation.tool.domain.Project;
import asia.ncc.estimation.tool.domain.ProjectType;

import java.util.List;

public class ProjectTypeDTO {

    private Long id;
    private String decription;
    private String technogory;
    private String name;

    private List<ProjectDTO> projectDTO;
    private List<NfrDTO> nfrDTOS;

    public List<NfrDTO> getNfrDTOS() {
        return nfrDTOS;
    }

    public void setNfrDTOS(List<NfrDTO> nfrDTOS) {
        this.nfrDTOS = nfrDTOS;
    }

    public List<ProjectDTO> getProjectDTO() {
        return projectDTO;
    }

    public void setProjectDTO(List<ProjectDTO> projectDTO) {
        this.projectDTO = projectDTO;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getTechnogory() {
        return technogory;
    }

    public void setTechnogory(String technogory) {
        this.technogory = technogory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
