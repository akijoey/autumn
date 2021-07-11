package com.akijoey.autumn.core.aop.intercept;

public abstract class Interceptor {

    private int order = -1;

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    public boolean supports(Object bean) {
        return false;
    }

    public abstract Object intercept(MethodInvocation methodInvocation);

}
