package asia.ncc.estimation.tool.service.dto;

public class WordItemDTO {
    private Long no;
    private String TaskName;
    private Long codingErffort;
    private int indayte;
    private String assumptionNote;
    private String assumptionConten;

    public Long getNo() {
        return no;
    }

    public WordItemDTO setNo(Long no) {
        this.no = no;
        return this;
    }

    public String getTaskName() {
        return TaskName;
    }

    public WordItemDTO setTaskName(String taskName) {
        TaskName = taskName;
        return this;
    }

    public Long getCodingErffort() {
        return codingErffort;
    }

    public WordItemDTO setCodingErffort(Long codingErffort) {
        this.codingErffort = codingErffort;
        return this;
    }

    public int getIndayte() {
        return indayte;
    }

    public WordItemDTO setIndayte(int indayte) {
        this.indayte = indayte;
        return this;
    }

    public String getAssumptionNote() {
        return assumptionNote;
    }

    public WordItemDTO setAssumptionNote(String assumptionNote) {
        this.assumptionNote = assumptionNote;
        return this;
    }

    public String getAssumptionConten() {
        return assumptionConten;
    }

    public WordItemDTO setAssumptionConten(String assumptionConten) {
        this.assumptionConten = assumptionConten;
        return this;
    }
}
