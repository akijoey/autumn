package com.akijoey.autumn.core.aop.intercept;

/**
 * bean 后置处理器
 */
public interface BeanPostProcessor {

    default Object postProcessAfterInitialization(Object bean) {
        return bean;
    }

}
