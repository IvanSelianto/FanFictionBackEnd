package com.fanfiction.services;

import com.fanfiction.models.Chapter;
import com.fanfiction.models.Composition;
import com.fanfiction.repository.ChapterRepository;
import com.fanfiction.repository.CompositionRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class PdfService {
    @Autowired
    private CompositionRepository compositionRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private final BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private final Font fontForDescription = new Font(bf, 14, 2);
    private final Font fontForTitle = new Font(bf, 20, 1);
    private final Font fontForContentHeader = new Font(bf, 16, 1);
    private final Font fontForChapterTitle = new Font(bf, 16, 1);

    public PdfService() throws IOException, DocumentException {
    }

    public void exportToPdf(Long compositionId, HttpServletResponse response) throws Exception {
        Composition composition = compositionRepository.getOne(compositionId);
        java.util.List<Chapter> chapters = chapterRepository.findAllByComposition(composition);
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        createTitleAndContent(document, composition, chapters);
        createChapters(chapters, document, writer);
        document.close();

    }


    private void createTitleAndContent(Document document, Composition composition, List<Chapter> chapters) throws DocumentException {
        Paragraph title = new Paragraph(composition.getTitle(), fontForTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph(composition.getDescription(), fontForDescription));
        document.add(Chunk.NEWLINE);
        Paragraph content = new Paragraph("Content", fontForContentHeader);
        content.setAlignment(Element.ALIGN_CENTER);
        document.add(content);
        document.add(Chunk.NEWLINE);
        for (int i = 0; i < chapters.size(); i++) {
            document.add(new Paragraph((i + 1) + ". " + chapters.get(i).getName(), fontForChapterTitle));
        }
    }


    private void createChapters(List<Chapter> chapters, Document document, PdfWriter writer) throws DocumentException, IOException {
        for (Chapter chapter : chapters) {
            document.newPage();
            Paragraph elements = new Paragraph(chapter.getName(), fontForChapterTitle);
            elements.setAlignment(Element.ALIGN_CENTER);
            document.add(elements);
            if (chapter.getImgUrl() != null) {
                Image chapterImage = Image.getInstance(chapter.getImgUrl());
                chapterImage.scaleToFit(400, 400);
                chapterImage.setAlignment(Element.ALIGN_CENTER);
                document.add(chapterImage);
            }
            parseHtml(chapter, writer, document);
        }
    }

    private void parseHtml(Chapter chapter, PdfWriter writer, Document document) throws IOException {
        String htmlContent = "<h4 style=\"font-family: arialuni, arial; font-size:14px; font-weight: normal; \" >"
                + chapter.getText() + "</h4>";
        XMLWorkerHelper.getInstance().parseXHtml(
                writer,
                document,
                new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)),
                null,
                StandardCharsets.UTF_8);
    }

}
