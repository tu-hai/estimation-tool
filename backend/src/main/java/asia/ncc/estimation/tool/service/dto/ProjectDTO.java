package asia.ncc.estimation.tool.service.dto;

import java.time.LocalDate;

public class ProjectDTO {
    private String id;
    private String name;
    private String category;
    private String size;
    private String techName;
    private String techDescripson;
    private String techContact;
    private LocalDate startdate;
    private LocalDate enddate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private LocalDate startDate;
    private LocalDate endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechDescripson() {
        return techDescripson;
    }

    public void setTechDescripson(String techDescripson) {
        this.techDescripson = techDescripson;
    }

    public String getTechContact() {
        return techContact;
    }

    public void setTechContact(String techContact) {
        this.techContact = techContact;
    }
}
