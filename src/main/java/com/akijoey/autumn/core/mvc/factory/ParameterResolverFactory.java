package com.akijoey.autumn.core.mvc.factory;

import com.akijoey.autumn.annotation.mvc.PathVariable;
import com.akijoey.autumn.annotation.mvc.RequestBody;
import com.akijoey.autumn.annotation.mvc.RequestHeader;
import com.akijoey.autumn.annotation.mvc.RequestParam;
import com.akijoey.autumn.core.mvc.resolver.*;

import java.lang.reflect.Parameter;

public class ParameterResolverFactory {

    public static ParameterResolver get(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return new RequestParamParameterResolver();
        }
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return new PathVariableParameterResolver();
        }
        if (parameter.isAnnotationPresent(RequestHeader.class)) {
            return new RequestHeaderParameterResolver();
        }
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return new RequestBodyParameterResolver();
        }
        return null;
    }

}
