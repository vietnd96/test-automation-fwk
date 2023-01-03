package com.ndviet.library.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.ndviet.library.configuration.Constants.APPLICATION_CONTEXT_PATH;

public class SpringHelpers {
    private static volatile SpringHelpers m_instance;
    private final String m_applicationContextPath;
    private final AbstractApplicationContext m_applicationContext;

    public SpringHelpers(String appContextFilePath) {
        m_applicationContextPath = appContextFilePath;
        m_applicationContext = new ClassPathXmlApplicationContext(m_applicationContextPath);
    }

    public static SpringHelpers getInstance() {
        if (m_instance == null) {
            synchronized (SpringHelpers.class) {
                if (m_instance == null) {
                    String fileClassPathLocation = System.getProperty(APPLICATION_CONTEXT_PATH);
                    m_instance = new SpringHelpers(fileClassPathLocation);
                }
            }
        }
        return m_instance;
    }

    public <T> T getBeanOfType(Class<T> tClass) {
        return m_applicationContext.getBeansOfType(tClass).values().iterator().next();
    }

    public <T> T getBean(String beanId) {
        return (T) m_applicationContext.getBean(beanId);
    }
}
