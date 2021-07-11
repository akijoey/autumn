package com.akijoey.autumn.core.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DependencyInjection {

    private static final Logger log = LoggerFactory.getLogger(DependencyInjection.class);

    /**
     * 遍历ioc容器所有bean的属性, 为所有带@Autowired/@Value注解的属性注入实例
     */
    public static void inject(String[] packageNames) {
        AutowiredBeanInitialization autowiredBeanInitialization = new AutowiredBeanInitialization(packageNames);
        Map<String, Object> beans = BeanFactory.BEANS;
        if (beans.size() > 0) {
            BeanFactory.BEANS.values().forEach(autowiredBeanInitialization::initialize);
        }
    }

}
