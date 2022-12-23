package com.ndviet.template;

import com.ndviet.file.FileUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateUtils {
    private static TemplateUtils m_instance;
    private Configuration m_freeMarkerConfig;
    private static String m_templatesDirectory;

    public static TemplateUtils getInstance() {
        if(m_instance == null)
            m_instance = new TemplateUtils();
        return m_instance;
    }

    public TemplateUtils() {
        m_freeMarkerConfig = getDefaultConfiguration();
    }

    public TemplateUtils(String templatesDirectory) throws IOException {
        m_freeMarkerConfig = getDefaultConfiguration();
        m_templatesDirectory = templatesDirectory;
        m_freeMarkerConfig.setDirectoryForTemplateLoading(new File(templatesDirectory));
    }

    private static Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        return configuration;
    }

    public static String processTemplate(String templateString, Map values) throws Exception {
        Configuration templateConfiguration = getDefaultConfiguration();
        Template template = new Template("template", new StringReader(templateString), templateConfiguration);
        StringWriter stringWriter = new StringWriter();
        template.process(values, stringWriter);
        return stringWriter.toString();
    }

    public static String processFileTemplate(String sourcePath, Object data, String targetPath) throws IOException, TemplateException {
        sourcePath = FileUtils.getPath(sourcePath);
        String templateName = sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1).trim();
        String templateDirectory = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)).trim();
        Configuration templateConfiguration = getDefaultConfiguration();
        templateConfiguration.setDirectoryForTemplateLoading(new File(templateDirectory));
        if(targetPath == null) {
            System.out.println(System.getProperty("user.dir") + File.separator + "target");
            File tempFile = File.createTempFile("output", "_" + templateName,
                    new File(System.getProperty("user.dir") + File.separator + "target"));
            targetPath = tempFile.getPath();
        }
        Template template = templateConfiguration.getTemplate(templateName);
        // File output
        Writer outputFile = null;
        try {
            outputFile = new FileWriter(targetPath);
            template.process(data, outputFile);
            outputFile.flush();
        } catch (IOException | TemplateException ex) {
            throw ex;
        } finally {
            if (null != outputFile) {
                outputFile.close();
            }
        }
        return targetPath;
    }

}