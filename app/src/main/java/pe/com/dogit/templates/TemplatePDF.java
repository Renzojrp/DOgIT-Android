package pe.com.dogit.templates;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import pe.com.dogit.activities.PDFVisorActivity;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fontSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fontText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fontHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
    private Font fontSubTitleTable = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.WHITE);


    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument(){
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

        } catch (Exception e) {
            Log.e("openDocument:" ,e.toString());
        }
    }

    private void createFile() {
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");

        if(!folder.exists()) {
            folder.mkdirs();
        }

        pdfFile = new File(folder, "TemplatePDF.pdf");
    }

    public void closeDocument() {
        document.close();
    }

    public void addMetaData(String title, String subject, String author) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, String date) {
        paragraph = new Paragraph();
        addChild(new Paragraph(title, fontTitle));
        addChild(new Paragraph(subTitle, fontSubTitle));
        addChild(new Paragraph(date, fontHighText));
        paragraph.setSpacingAfter(30);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addTitles:" ,e.toString());
        }
    }

    private void addChild(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text) {
        paragraph = new Paragraph(text, fontText);
        paragraph.setSpacingAfter(5);
        paragraph.setSpacingBefore(5);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addParagraph:" ,e.toString());
        }
    }

    public void createTable(String[]header, ArrayList<String[]>body) {
        paragraph = new Paragraph();
        paragraph.setFont(fontText);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.setSpacingBefore(20);
        PdfPCell pdfPCell;
        int indexC = 0;
        while (indexC < header.length) {
            pdfPCell = new PdfPCell(new Phrase(header[indexC++], fontSubTitleTable));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.BLACK);
            pdfPTable.addCell(pdfPCell);
        }

        for(int indexR = 0; indexR < body.size(); indexR++) {
            String[]row = body.get(indexR);
            for(indexC = 0; indexC < header.length; indexC++) {
                pdfPCell = new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);
                pdfPTable.addCell(pdfPCell);
            }
        }

        paragraph.add(pdfPTable);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("createTable:" ,e.toString());
        }
    }

    public void viewPDF() {
        Intent intent = new Intent(context, PDFVisorActivity.class);
        intent.putExtra("path", pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
