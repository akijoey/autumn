package com.akijoey.autumn.core.ioc;

import com.akijoey.autumn.annotation.ioc.Component;

public class BeanHelper {

    /**
     * get the bean name
     *
     * @param aClass target class
     * @return the bean name
     */
    public static String getBeanName(Class<?> aClass) {
        String beanName = aClass.getName();
        if (aClass.isAnnotationPresent(Component.class)) {
            Component component = aClass.getAnnotation(Component.class);
            beanName = "".equals(component.name()) ? aClass.getName() : component.name();
        }
        return beanName;
    }

}
