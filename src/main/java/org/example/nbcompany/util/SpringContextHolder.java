package org.example.nbcompany.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文持有者，用于在静态方法中获取Spring Bean
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }
    
    /**
     * 获取ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     * 根据Bean名称获取Bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
    
    /**
     * 根据Bean类型获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
    
    /**
     * 根据Bean名称和类型获取Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
} 