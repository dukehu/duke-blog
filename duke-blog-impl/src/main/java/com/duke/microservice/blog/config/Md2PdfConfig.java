package com.duke.microservice.blog.config;

import com.duke.framework.CoreConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created duke on 2018/10/26
 */
@Configuration
@Slf4j
public class Md2PdfConfig implements CommandLineRunner {


    private static String PDF_FOLDER_PATH;

    static {
        PDF_FOLDER_PATH = System.getProperty(CoreConstants.USERDIR)
                + File.separator + "tmp" + File.separator;
    }

    public static String getPdfFolderPath() {
        return PDF_FOLDER_PATH;
    }

    @Override
    public void run(String... strings) throws Exception {
        File file = new File(Md2PdfConfig.getPdfFolderPath());
        if (!file.exists()) {
            if (file.mkdirs()) {
                log.info("create folder name of 'tmp' successful");
            } else {
                log.error("create folder name of 'tmp' fail");
            }
        }
        File pdfFile = new File(Md2PdfConfig.getPdfFolderPath() + "template.pdf");
        if (!pdfFile.exists()) {
            if (pdfFile.createNewFile()) {
                log.info("create folder name of 'template.pdf' successful");
            } else {
                log.error("create folder name of 'template.pdf' fail");
            }
        }
    }
}
