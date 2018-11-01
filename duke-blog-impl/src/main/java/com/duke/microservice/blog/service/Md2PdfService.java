package com.duke.microservice.blog.service;

import com.duke.framework.utils.FileReadUtils;
import com.duke.framework.utils.FileWriteUtils;
import com.duke.microservice.blog.config.Md2PdfConfig;
import com.duke.microservice.blog.utils.Html2PdfUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created duke on 2018/10/25
 */
@Service
public class Md2PdfService {

    private static String HTML_PREFIX;
    private static String HTML_SUFFIX;

    static {
        try {
            HTML_SUFFIX = "</div>\n" +
                    "</body>\n" +
                    "</html>";
            HTML_PREFIX = FileReadUtils.readAll("md2pdf/mdcss/github-md.css");
            HTML_PREFIX = "<html>\n" +
                    "<head>\n" +
                    "<style type=\"text/css\">\n"
                    + HTML_PREFIX + "\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style=\"font-family: SimSun\" class=\"markdown-body\">\n";
        } catch (Exception e) {
            HTML_PREFIX = "";
        }
    }

    /**
     * 根据内容转换
     *
     * @param htmlContent html_content
     * @param title       标题
     */
    public String convertOfContent(String htmlContent, String title) {
        String content = "<h1><center>" + title + "</center></h1>" + htmlContent;
        content = HTML_PREFIX + content + HTML_SUFFIX;
        InputStream stream = new ByteArrayInputStream(content.getBytes());
        try {
            FileWriteUtils.saveFileByStream(stream, Md2PdfConfig.getPdfFolderPath(), title, "html");
            Html2PdfUtils.ofFile(Md2PdfConfig.getPdfFolderPath() + title + ".html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Md2PdfConfig.getPdfFolderPath() + "template.pdf";
    }
}
