package com.duke.microservice.blog.utils;

import com.duke.framework.domain.MarkdownEntity;
import com.duke.framework.utils.FileReadUtils;
import com.google.common.base.Joiner;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created duke on 2018/10/25
 */
public class Md2HtmlUtils {
    private static String HTML_PREFIX;

    static {
        try {
            HTML_PREFIX = FileReadUtils.readAll("md2pdf/mdcss/github-md.css");
            HTML_PREFIX = "<html>\n<head>\n<style type=\"text/css\">\n" + HTML_PREFIX + "\n</style>\n</head>\n<body>\n";
        } catch (Exception e) {
            HTML_PREFIX = "";
        }
    }


    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return MarkdownEntity
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtils.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return MarkdownEntity
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtils.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream 流
     * @return MarkdownEntity
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(lines);
        return ofContent(content);
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return MarkdownEntity
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setHtmlPrefix(HTML_PREFIX);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body");
        entity.addDivStyle("style", "font-family: SimSun");
        return entity;
    }


    /**
     * markdown to image
     *
     * @param content markdown contents
     * @return parse html contents
     */
    private static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        // enable table parse!
        options.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
