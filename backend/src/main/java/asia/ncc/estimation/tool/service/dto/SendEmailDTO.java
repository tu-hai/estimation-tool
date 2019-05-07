package asia.ncc.estimation.tool.service.dto;

import java.util.List;

public class SendEmailDTO {
        private  Long customerId;
        private Long quotationId;
    	private  String subbject;
    	private  String text;
    	private List<String> fileAttachment;

    public Long getQuotationId() {
        return quotationId;
    }

    public SendEmailDTO setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
        return this;
    }

    public List<String> getFileAttachment() {
        return fileAttachment;
    }

    public SendEmailDTO setFileAttachment(List<String> fileAttachment) {
        this.fileAttachment = fileAttachment;
        return this;
    }

    public String getSubbject() {
        return subbject;
    }

    public void setSubbject(String subbject) {
        this.subbject = subbject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
