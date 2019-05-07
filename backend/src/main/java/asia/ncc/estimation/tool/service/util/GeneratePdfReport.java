package asia.ncc.estimation.tool.service.util;

import asia.ncc.estimation.tool.domain.*;
import asia.ncc.estimation.tool.service.dto.PdfDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
public class GeneratePdfReport {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
        Font.BOLD);
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD,BaseColor.CYAN);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.NORMAL);

    public List<FilePdf> pdf(PdfDTO pdfDTo, String dirPath) {
        System.out.println("DirPath pdf: "+dirPath);
        List<FilePdf> filePdfs = new ArrayList<>();
        FilePdf filePdf = new FilePdf();
        ByteArrayOutputStream outputStream = null;
        Document document,document1;
        File directory = new File(dirPath);
        if (! directory.exists()){
            directory.mkdirs();
        }
        try {
            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            document = new Document();
            PdfWriter.getInstance(document,outputStream);

            document.open();
            if(pdfDTo.getAssumptions() !=null)
                document = wbsPdfReport(pdfDTo.getAssumptions(),pdfDTo.getProjects(),document);
            if (pdfDTo.getExtracEfforts()!=null)
                document=extracEffortReport(pdfDTo.getProjects(),document);
            if(pdfDTo.getProjects()!=null)
              document= totalDocument(pdfDTo.getProjects(),document);
            document.close();

            byte[] bytes = outputStream.toByteArray();
            LocalDate localDate = LocalDate.now();
            FileOutputStream fos = new FileOutputStream(dirPath+"/"+"Report-Quotation.pdf");
            System.out.println("Create Quotation pdf in: "+dirPath+"/"+"Report-Quotation.pdf");
            fos.write(bytes);

            dirPath.replace('\\', '/');
            filePdf.setLocation(dirPath+"/");
            filePdf.setName("Report-Quotation.pdf");
            filePdf.setDateCreate(localDate);
            filePdfs.add(filePdf);

                if(pdfDTo.getNfrs()!=null&&pdfDTo.getProjects()!=null){
                List<Project> projects= pdfDTo.getProjects();
                List<NFR> nfrs = pdfDTo.getNfrs();
                int i=0;
                for(Project project:projects) {
                    document1= new Document();
                    filePdf=new FilePdf();
                    i++;
                    ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
                    PdfWriter.getInstance(document1, outputStream1);
                    List<NFR> nfrList = new ArrayList<>();
                    for (NFR nfr:nfrs) {
                        if (project.getProjecTypeId().getId() == nfr.getProjectType().getId()) {
                            nfrList.add(nfr);
                        }
                    }
                    document1.open();
                    document1.setPageSize(PageSize.A4.rotate());
                    document1 = nfrDocument(nfrList, document1);
                    document1.setPageSize(PageSize.A4);
                    document1.close();

                    FileOutputStream fos1 = new FileOutputStream(dirPath+"/"+"Report-NFR-"+i+".pdf");
                    System.out.println("Create NFR pdf in: "+dirPath+"\\"+"Report-NFR-"+i+".pdf");
                    fos1.write(outputStream1.toByteArray());

                    filePdf.setLocation(dirPath+"/");
                    filePdf.setName("Report-NFR-"+i+".pdf");
                    filePdf.setDateCreate(localDate);
                    filePdfs.add(filePdf);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            //clean off
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
        return filePdfs;
    }
    public static Document totalDocument( List<Project> projects, Document document){
        if(document==null){
            document=new Document();
        }

        try {
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 3);
            preface.add(new Paragraph("Total Report", catFont));
            addEmptyLine(preface, 3);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{4,2,2,3});

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Project", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Total CodingEffort", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Total PercentEffort", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Total", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);
            for (Project project: projects){
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(project.getName()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);

                double sWBS=0;
                double sExtra=0;
                double sum=0;
                List<WorkItem> workItemList=new ArrayList<>(project.getWorkItemIds());
                List<ExtracEffort> extracEffortList =new ArrayList<>(project.getEtracEffortIds());
                for(WorkItem workItem:workItemList){

                    sWBS=sWBS+workItem.getCodingEffort();
                }
                for (ExtracEffort extracEffort:extracEffortList){
                    sExtra=sExtra+extracEffort.getPercentEfforts();
                }
                sum=sWBS+sWBS*(sExtra/100);
                DecimalFormat dcf = new DecimalFormat("#");
                cell = new PdfPCell(new Phrase(String.valueOf(dcf.format(sWBS))));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase(String.valueOf(dcf.format(sExtra))+"%"));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(sum)));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);
            }

            document.newPage();
            document.add(preface);
            document.add(table);
        }catch (DocumentException ex){
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

    public static List<Project> projectList(List<Project> projects){
        List<Project> listfix = new ArrayList<>();

        for (Project project:projects){
            if(listfix.size()<1) {
                listfix.add(project);
            }
            int i=0;
            for (Project test:listfix){
                if(test.getProjecTypeId()==project.getProjecTypeId()){
                  i++;
                }
            }
            if(i==0) listfix.add(project);
        }
        return listfix;
    }


    public static Document nfrDocument(List<NFR> nfrs, Document document) {
    if(document==null){
        document=new Document();
    }

    try {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("Standard non-functional requirements Report", catFont));
        addEmptyLine(preface, 3);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2,4,3,3,3,2,3,4});

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("No.", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Feature", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Category", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Standard", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Applicable (Y/N)", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Effort", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Comment", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Guidance", subFont));
        hcell.setBackgroundColor(BaseColor.DARK_GRAY);
        hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setPadding(7);
        table.addCell(hcell);

        Integer i = 0;
        for (NFR nfr : nfrs) {
            i++;

            PdfPCell cell;

            cell = new PdfPCell(new Phrase(i.toString(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getName(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getCategory(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getStandard(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getApplicable(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getEffort(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getComment(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nfr.getGuidance(),smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7);
            table.addCell(cell);

        }
        document.newPage();
        document.add(preface);
        document.add(table);

    } catch (DocumentException ex) {

        Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
    }
    return document;
    }

    public static Document wbsPdfReport(List<Assumption> assumptionList,List<Project> projects,Document document) {
        if(document==null){
            document=new Document();
        }

        try {
            Paragraph preface = new Paragraph();
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2,4,4,2,2,2,4});
            PdfPCell cellnull=new PdfPCell(new Phrase(""));
            preface.add(new Paragraph("Report WBS", catFont));
           /* preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));*/
            addEmptyLine(preface,2);
            //   Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            addEmptyLine(preface, 1);

            PdfPCell hcell = new PdfPCell();

            hcell = new PdfPCell(new Phrase(" No" , subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("TaskName", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Actual Effort", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell=new PdfPCell(new Phrase("Assumption", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Note", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Coding Effort", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Indays", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setPadding(7);
            table.addCell(hcell);


            for (Project project: projects) {
                hcell = new PdfPCell(new Phrase("Project Name: "));
                hcell.setColspan(2);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase(project.getName()));
                hcell.setColspan(5);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);
                List<WorkItem> workItemList = new ArrayList<>(project.getWorkItemIds());
                int no = 1;
                for (WorkItem workItem : workItemList) {

                    PdfPCell cell;

                    cell = new PdfPCell(new Phrase(String.valueOf(no)));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);
                    no++;

                    cell = new PdfPCell(new Phrase(workItem.getTaskname()));
                    if (workItem.getTaskname()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(workItem.getActualeffort())));
                    if (workItem.getActualeffort()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                    for (Assumption assumption : assumptionList) {
                        if ((workItem.getId() == assumption.getWorkItem().getId())) {
                            cell = new PdfPCell(new Phrase(String.valueOf(assumption.getContent())));
                            if (assumption.getContent()==null)cell=cellnull;
                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setPadding(7);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(assumption.getNote()));
                            if (workItem.getNote()==null)cell=cellnull;
                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setPadding(7);
                            table.addCell(cell);
                            break;
                        }
                    }
                    cell = new PdfPCell(new Phrase(workItem.getCodingEffort().toString()));
                    if (workItem.getCodingEffort()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(workItem.getIndates())));
                    if (workItem.getIndates()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                }

                long s = 0;
                long a = 0;
                for (WorkItem workItem : workItemList) {
                    if (workItem.getCodingEffort()!=null)
                    s = s + workItem.getCodingEffort();
                    if (workItem.getIndates()!=null)
                    a = a + workItem.getIndates();
                }
                PdfPCell cell;

                hcell = new PdfPCell(new Phrase("Total"));
                hcell.setColspan(5);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);


                cell = new PdfPCell(new Phrase(String.valueOf(s)));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);//CodingEfeec

                cell = new PdfPCell(new Phrase(String.valueOf(a)));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);//Indays

            }
                document.newPage();
                document.add(preface);
                document.add(table);

        } catch (DocumentException ex) {

            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return document;
    }
    public static Document extracEffortReport(List<Project> list,Document doc) {

        if(doc==null){
            doc=new Document();}


        try {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 2, 4, 5, 4 });
            PdfPCell cellnull=new PdfPCell(new Phrase(""));
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            Font tilteFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, BaseColor.BLACK);
            Font contentFont = FontFactory.getFont(FontFactory.TIMES, 12);

            Paragraph paragraph =  new Paragraph("LIST EXTRACEFFORT", tilteFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(paragraph, 3);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("ID", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Name", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Description", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setPadding(7);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Percent Effort", subFont));
            hcell.setBackgroundColor(BaseColor.DARK_GRAY);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setPadding(7);
            table.addCell(hcell);

            for (Project project: list) {
                hcell = new PdfPCell(new Phrase("Project Name: "));
                hcell.setColspan(2);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase(project.getName()));
                hcell.setColspan(2);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);

                Set<ExtracEffort> extracEffortList=project.getEtracEffortIds();
                long s=0;
                int i=0;
                for (ExtracEffort extrac : extracEffortList) {
                    PdfPCell cell;
                    i++;
                    cell = new PdfPCell(new Phrase(String.valueOf(i)));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(extrac.getName(), contentFont));
                    if (extrac.getName()==null)cell=cellnull;
                    cell.setPadding(7);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(extrac.getDecription(), contentFont));
                    if (extrac.getDecription()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(extrac.getPercentEfforts().toString() + "%", contentFont));
                    if (extrac.getPercentEfforts()==null)cell=cellnull;
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(7);
                    table.addCell(cell);
                    s = s + extrac.getPercentEfforts();

                }

                PdfPCell cell;

                hcell = new PdfPCell(new Phrase("Total"));
                hcell.setColspan(3);
                hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(7);
                table.addCell(hcell);

                cell = new PdfPCell(new Phrase(String.valueOf(s) + "%", contentFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                table.addCell(cell);
            }
            doc.newPage();
            doc.add(paragraph);
            doc.add(table);

        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);

        }
        return doc;
    }

}
