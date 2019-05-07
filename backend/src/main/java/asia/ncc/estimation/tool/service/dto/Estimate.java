package asia.ncc.estimation.tool.service.dto;

import asia.ncc.estimation.tool.domain.WorkItem;

import java.util.List;

public class Estimate {
    private List<WordItemDTO> listword;
    private Long toltalExfort ;
    private Long inDate;

    public List<WordItemDTO> getListword() {
        return listword;
    }

    public void setListword(List<WordItemDTO> listword) {
        this.listword = listword;
    }

    public Long getToltalExfort() {
        return toltalExfort;
    }

    public void setToltalExfort(Long toltalExfort) {
        this.toltalExfort = toltalExfort;
    }

    public Long getInDate() {
        return inDate;
    }

    public void setInDate(Long inDate) {
        this.inDate = inDate;
    }
}
