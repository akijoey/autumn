package com.akijoey.autumn.core.aop.intercept;

import com.akijoey.autumn.core.aop.proxy.JdkAspectProxy;

/**
 * JDK implementation of dynamic proxy
 */
public class JdkAopProxyBeanPostProcessor extends AbstractAopProxyBeanPostProcessor {


    @Override
    public Object wrapBean(Object target, Interceptor interceptor) {
        return JdkAspectProxy.wrap(target, interceptor);
    }

}