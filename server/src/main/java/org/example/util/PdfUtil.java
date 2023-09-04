package org.example.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PdfUtil {

    public static byte[] toPdf(String windows1251) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            URL resource = PdfUtil.class.getClassLoader().getResource("anon.ttf");
            File fontFile = new File(Objects.requireNonNull(resource).getFile());
            PDType0Font customFont = PDType0Font.load(document, fontFile);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(customFont, 10);

            float yPosition = 750; // Initial y-position

            Charset windows1251Charset = Charset.forName("Windows-1251");
            byte[] windows1251Bytes = windows1251.getBytes(windows1251Charset);
            String unicodeString = new String(windows1251Bytes, StandardCharsets.UTF_8);

            String[] lines = unicodeString.split("\n");

            for (String line : lines) {
                contentStream.beginText();
                contentStream.newLine();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(line);
                contentStream.endText();

                yPosition -= 15;
            }

            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }

    }
}
