package com.akijoey.autumn.core.aop.intercept;

import com.akijoey.autumn.common.util.ReflectionUtil;

import java.lang.reflect.Method;

public class MethodInvocation {

    //target object
    private final Object targetObject;
    //target method
    private final Method targetMethod;
    //the parameter of target method
    private final Object[] args;

    public MethodInvocation(Object targetObject, Method targetMethod, Object[] args) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.args = args;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object proceed() {
        return ReflectionUtil.executeTargetMethod(targetObject, targetMethod, args);
    }

}
