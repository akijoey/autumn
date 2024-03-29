package com.akijoey.autumn.core.aop.intercept;

import com.akijoey.autumn.annotation.aop.After;
import com.akijoey.autumn.annotation.aop.Before;
import com.akijoey.autumn.annotation.aop.Pointcut;
import com.akijoey.autumn.common.util.ReflectionUtil;
import com.akijoey.autumn.core.aop.lang.JoinPoint;
import com.akijoey.autumn.core.aop.lang.JoinPointImpl;
import com.akijoey.autumn.core.aop.util.PatternMatchUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class InternallyAspectInterceptor extends Interceptor {

    private final Object adviceBean;
    private final HashSet<String> expressionUrls = new HashSet<>();
    private final List<Method> beforeMethods = new ArrayList<>();
    private final List<Method> afterMethods = new ArrayList<>();

    public InternallyAspectInterceptor(Object adviceBean) {
        this.adviceBean = adviceBean;
        init();
    }

    private void init() {
        for (Method method : adviceBean.getClass().getMethods()) {
            Pointcut pointcut = method.getAnnotation(Pointcut.class);
            if (!Objects.isNull(pointcut)) {
                expressionUrls.add(pointcut.value());
            }
            Before before = method.getAnnotation(Before.class);
            if (!Objects.isNull(before)) {
                beforeMethods.add(method);
            }
            After after = method.getAnnotation(After.class);
            if (!Objects.isNull(after)) {
                afterMethods.add(method);
            }
        }
    }

    @Override
    public boolean supports(Object bean) {
        return expressionUrls.stream().anyMatch(url -> PatternMatchUtils.simpleMatch(url, bean.getClass().getName())) && (!beforeMethods.isEmpty() || !afterMethods.isEmpty());
    }

    @Override
    public Object intercept(MethodInvocation methodInvocation) {
        JoinPoint joinPoint = new JoinPointImpl(adviceBean, methodInvocation.getTargetObject(),
                methodInvocation.getArgs());
        beforeMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, joinPoint));
        Object result = methodInvocation.proceed();
        afterMethods.forEach(method -> ReflectionUtil.executeTargetMethodNoResult(adviceBean, method, result, joinPoint));
        return result;
    }

}
