package asia.ncc.estimation.tool.service.dto;

import java.util.List;

public class ListQDTO {
    private List<QuotationDTO> content;
    private long totalPages;
    private long totalElements;

    public List<QuotationDTO> getContent() {
        return content;
    }

    public ListQDTO setContent(List<QuotationDTO> content) {
        this.content = content;
        return this;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public ListQDTO setTotalPages(long totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public ListQDTO setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }
}
