package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.domain.*;
import asia.ncc.estimation.tool.repository.*;
import asia.ncc.estimation.tool.service.MailService;
import asia.ncc.estimation.tool.service.dto.PdfDTO;
import asia.ncc.estimation.tool.service.dto.SendEmailDTO;
import asia.ncc.estimation.tool.service.util.GeneratePdfReport;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import asia.ncc.estimation.tool.web.rest.util.MediaTypeUtils;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CreatePDFResource {
    private static final String DIRECTORY = "/testupload";
    private static final String DEFAULT_FILE_NAME = "MySQL.pdf";
    @Autowired
    private AssumptionRepository assumptionRepository;
    @Autowired
    private WorkItemRepository workItemRepository;
    @Autowired
    private NFRRepository nfrRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ExtracEffortRepository extracEffortRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private FilePdfRepository filePdfRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private FilePdfResource filePdfResource;
    @GetMapping("/pdfreport")
    public List<FilePdf> PDFReport() {

        List<Assumption> assumptions=  assumptionRepository.findAll();
        List<WorkItem> workItemList =  workItemRepository.findAll();
        List<NFR> nfrs = nfrRepository.findAll();
        List<ExtracEffort> extracEfforts=extracEffortRepository.findAll();
        List<Project> projects=projectRepository.findAll();
        PdfDTO pdfDTO = new PdfDTO();
        pdfDTO.setAssumptions(assumptions);
        pdfDTO.setWorkItems(workItemList);
        pdfDTO.setNfrs(nfrs);
        pdfDTO.setExtracEfforts(extracEfforts);
        pdfDTO.setProjects(projects);

        GeneratePdfReport generatePdfReport= new GeneratePdfReport();
        String dir="";
        String osname = System.getProperty("os.name", "").toLowerCase();
        System.out.println("OS name: "+osname);
        if (osname.startsWith("windows")) {
            //dir=DIRECTORY;
            dir= System.getProperty("user.dir")+DIRECTORY;
            System.out.println("DIRECTORY set: "+dir);
        }else if (osname.startsWith("linux")){
            //dir= System.getProperty("user.dir")+"\\src\\main\\resources\\fileupload";
            //dir= System.getProperty("user.dir")+DIRECTORY;
            dir=DIRECTORY;
            System.out.println("DIRECTORY set: "+dir);
        }

        List<FilePdf> filePdfs = generatePdfReport.pdf(pdfDTO, dir);
        for (FilePdf filePdf:filePdfs) {
            if (filePdf.getName() != null) {
                System.out.println("FilePdf: "+filePdf);
                filePdfRepository.save(filePdf);
                String dir2 = dir + "\\" + filePdf.getName();
                dir2 = (dir2).replace('\\', '/');
                System.out.println("\n\n ++++++++++++++ :    " + dir2);
            }
        }
        return filePdfs;
    }
    @GetMapping("/pdfreport/{idQuotation}")
    public List<FilePdf> PDFReport(@PathVariable  Long idQuotation) {
        Optional<Quotation> quotationOptional = quotationRepository.findById(idQuotation);
        Quotation quotation =quotationOptional.get();
        PdfDTO pdfDTO = new PdfDTO();
        List<Assumption> assumptions = assumptionRepository.findAll();
        Set<WorkItem> workItemList = quotation.getProjectID().getWorkItemIds();
        List<WorkItem> workItems=new ArrayList<>(workItemList);
        Set<ExtracEffort> extracEfforts = quotation.getProjectID().getEtracEffortIds();
        List<ExtracEffort> extracEfforts1=new ArrayList<>(extracEfforts);
        List<Project> projects = new ArrayList<>();
        projects.add(quotation.getProjectID());
        List<NFR> nfrs=nfrRepository.findAll();

        pdfDTO.setAssumptions(assumptions);
        pdfDTO.setWorkItems(workItems);
        pdfDTO.setNfrs(nfrs);
        pdfDTO.setExtracEfforts(extracEfforts1);
        pdfDTO.setProjects(projects);

        GeneratePdfReport generatePdfReport = new GeneratePdfReport();
        String dir="";
        String osname = System.getProperty("os.name", "").toLowerCase();
        System.out.println("OS name: "+osname);
        if (osname.startsWith("windows")) {
            //dir=DIRECTORY;
            dir= System.getProperty("user.dir")+DIRECTORY;
            System.out.println("DIRECTORY set: "+dir);
        }else if (osname.startsWith("linux")){
            //dir= System.getProperty("user.dir")+"\\src\\main\\resources\\fileupload";
            //dir= System.getProperty("user.dir")+DIRECTORY;
            dir=DIRECTORY;
            System.out.println("DIRECTORY set: "+dir);
        }
        List<FilePdf> filePdfs = generatePdfReport.pdf(pdfDTO, dir);
        for (FilePdf filePdf : filePdfs) {
            if (filePdf.getName() != null) {
                System.out.println(filePdf);
                filePdf.setIdCustomer(quotation.getCustomerId().getId());
                filePdf.setIdQuotation(quotation.getId());
                filePdfRepository.save(filePdf);
                String dir2 = filePdf.getLocation() + "\\" + filePdf.getName();
                dir2 = (dir2).replace('\\', '/');
                System.out.println("\n\n ++++++++++++++ :    " + dir2);
            }
        }
        return filePdfs;
    }

    @PostMapping("/sendTocustomer")
    public ResponseEntity<SendEmailDTO> sendEmail(@RequestBody SendEmailDTO sendEmail) throws DocumentException {
        List<FilePdf> filePdfs = PDFReport(sendEmail.getQuotationId());
        List<String> listPath = new ArrayList<>();
        for(FilePdf filePdf:filePdfs){
            listPath.add(filePdf.getLocation()+filePdf.getName());
            filePdf.setIdCustomer(sendEmail.getCustomerId());
            filePdf.setIdQuotation(sendEmail.getQuotationId());
            filePdfRepository.save(filePdf);
        }
        sendEmail.setFileAttachment(listPath);
        mailService.sendMaiToCusTomer(sendEmail);

        Optional<Customer> customerOptional = customerRepository.findById(sendEmail.getCustomerId());
        System.out.println("Send Email  to {}!"+customerOptional.get().getEmail());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Send Email Successfully !",customerOptional.get().toString())).body(sendEmail);
    }
    @GetMapping("/exportpdf/{id}")
    public List<FilePdf> exportpdf(@PathVariable Long id) {
        System.out.println("Xóa file pdf: "+filePdfResource.deleteallFilePdf());
        return PDFReport(id);
    }
    public ResponseEntity<ByteArrayResource> download(FilePdf filePdf) throws IOException {
        String fileName=filePdf.getName();
        String fileDir =filePdf.getLocation();
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
        Path path;
        if(fileDir!=null&&fileName != null) {
            path = Paths.get(fileDir + "/" + fileName);
        }else{
            path = Paths.get(DIRECTORY + "/" + DEFAULT_FILE_NAME);
        }
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
            // Content-Disposition
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
            // Content-Type
            .contentType(mediaType) //
            // Content-Lengh
            .contentLength(data.length) //
            .body(resource);
    }
    @GetMapping("/pathtest")
    public void testpath(HttpServletRequest request){
        System.out.println("Remote: "+request.getRemoteUser());
        System.out.println("URI: "+request.getRequestURI());
        System.out.println("AuthType: "+request.getAuthType());
        System.out.println("ConTextPath: "+request.getContextPath());
        System.out.println("pathInfo: "+request.getPathInfo());
        System.out.println("URL: "+request.getRequestURL());
        System.out.println("Path: "+request.getServletPath());
        System.out.println("Server:port"+request.getServerName()+":"+request.getServerPort());
    }
    @GetMapping("/downloadpdf/{id}")
    public ResponseEntity<ByteArrayResource> downloadpdf(@PathVariable Long id) throws IOException {
        Optional<FilePdf> filePdfOptional = filePdfRepository.findById(id);
        FilePdf filePdf=filePdfOptional.get();
        return download(filePdf);
    }
    @GetMapping("/getpdfbyquotation/{id}")
    public List<FilePdf> getPdfByQuo(@PathVariable Long id){
        List<FilePdf> filePdfs= new ArrayList<>();
        List<FilePdf> allfilePdf = filePdfRepository.findAll();
        for (FilePdf filePdf:allfilePdf){
            if (filePdf.getIdQuotation()==id){
                filePdfs.add(filePdf);
            }
        }
        return filePdfs;
    }
}

