package com.akijoey.autumn.core.ioc;

import com.akijoey.autumn.annotation.ioc.Component;
import com.akijoey.autumn.annotation.mvc.RestController;
import com.akijoey.autumn.common.util.ReflectionUtil;
import com.akijoey.autumn.core.aop.factory.AopProxyBeanPostProcessorFactory;
import com.akijoey.autumn.core.aop.intercept.BeanPostProcessor;
import com.akijoey.autumn.core.config.ConfigurationFactory;
import com.akijoey.autumn.core.config.ConfigurationManager;
import com.akijoey.autumn.exception.DoGetBeanException;
import com.akijoey.autumn.factory.ClassFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BeanFactory {

    // ioc bean 容器
    public static final Map<String, Object> BEANS = new ConcurrentHashMap<>(128);

    private static final Map<String, String[]> SINGLE_BEAN_NAMES_TYPE_MAP = new ConcurrentHashMap<>(128);

    public static void loadBeans() {
        ClassFactory.CLASSES.get(Component.class).forEach(aClass -> {
            String beanName = BeanHelper.getBeanName(aClass);
            Object obj = ReflectionUtil.newInstance(aClass);
            BEANS.put(beanName, obj);
        });
        ClassFactory.CLASSES.get(RestController.class).forEach(aClass -> {
            Object obj = ReflectionUtil.newInstance(aClass);
            BEANS.put(aClass.getName(), obj);
        });
        BEANS.put(ConfigurationManager.class.getName(), new ConfigurationManager(ConfigurationFactory.getConfig()));
    }

    public static void applyBeanPostProcessors() {
        BEANS.replaceAll((beanName, beanInstance) -> {
            BeanPostProcessor beanPostProcessor = AopProxyBeanPostProcessorFactory.get(beanInstance.getClass());
            return beanPostProcessor.postProcessAfterInitialization(beanInstance);
        });
    }

    public static <T> T getBean(Class<T> type) {
        String[] beanNames = getBeanNamesForType(type);
        if (beanNames.length == 0) {
            throw new DoGetBeanException("not fount bean implement，the bean :" + type.getName());
        }
        Object beanInstance = BEANS.get(beanNames[0]);
        if (!type.isInstance(beanInstance)) {
            throw new DoGetBeanException("not fount bean implement，the bean :" + type.getName());
        }
        return type.cast(beanInstance);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        String[] beanNames = getBeanNamesForType(type);
        for (String beanName : beanNames) {
            Object beanInstance = BEANS.get(beanName);
            if (!type.isInstance(beanInstance)) {
                throw new DoGetBeanException("not fount bean implement，the bean :" + type.getName());
            }
            result.put(beanName, type.cast(beanInstance));
        }
        return result;
    }

    private static String[] getBeanNamesForType(Class<?> type) {
        String beanName = type.getName();
        String[] beanNames = SINGLE_BEAN_NAMES_TYPE_MAP.get(beanName);
        if (beanNames == null) {
            List<String> beanNamesList = new ArrayList<>();
            for (Map.Entry<String, Object> beanEntry : BEANS.entrySet()) {
                Class<?> beanClass = beanEntry.getValue().getClass();
                if (type.isInterface()) {
                    Class<?>[] interfaces = beanClass.getInterfaces();
                    for (Class<?> c : interfaces) {
                        if (type.getName().equals(c.getName())) {
                            beanNamesList.add(beanEntry.getKey());
                            break;
                        }
                    }
                } else if (beanClass.isAssignableFrom(type)) {
                    beanNamesList.add(beanEntry.getKey());
                }
            }
            beanNames = beanNamesList.toArray(new String[0]);
            SINGLE_BEAN_NAMES_TYPE_MAP.put(beanName, beanNames);
        }
        return beanNames;
    }

}
