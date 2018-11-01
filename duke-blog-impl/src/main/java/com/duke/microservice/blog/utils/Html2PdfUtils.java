package com.duke.microservice.blog.utils;

import com.duke.microservice.blog.config.Md2PdfConfig;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created duke on 2018/10/25
 */
public class Html2PdfUtils {

    public static void ofFile(String inputFile) throws IOException, DocumentException {

        OutputStream os = new FileOutputStream(Md2PdfConfig.getPdfFolderPath() + "template.pdf");
        ITextRenderer renderer = new ITextRenderer();
        String url = new File(inputFile).toURI().toURL().toString();

        renderer.setDocument(url);

        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("md2pdf/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(os);

        os.flush();
        os.close();
    }

}
