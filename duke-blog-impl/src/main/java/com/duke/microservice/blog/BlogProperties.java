package com.duke.microservice.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created duke on 2018/7/2
 */
@Configuration
@ConfigurationProperties(prefix = "duke.blog", ignoreInvalidFields = true)
@Getter
@Setter
public class BlogProperties {

    private Storage storage = new Storage();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Storage {
        private String path;

        private long maxUploadSize;

        private List<String> textType;

        private List<String> imgType;

        private List<String> officeType;

        private List<String> compressType;

        private List<String> pdfType;

        private Qiniu qiniu = new Qiniu();

        /**
         * 七牛云配置
         */
        @Setter
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        class Qiniu {
            private String accessKey;

            private String secretKey;

            private String bucket;

            private String path;
        }
    }
}
