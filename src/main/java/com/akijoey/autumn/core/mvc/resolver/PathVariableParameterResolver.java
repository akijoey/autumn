package com.akijoey.autumn.core.mvc.resolver;

import com.akijoey.autumn.annotation.mvc.PathVariable;
import com.akijoey.autumn.common.util.ObjectUtil;
import com.akijoey.autumn.core.mvc.entity.MethodDetail;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * process @PathVariable annotation
 *
 **/
public class PathVariableParameterResolver implements ParameterResolver {

    @Override
    public Object resolve(MethodDetail methodDetail, Parameter parameter) {
        PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
        String requestParameter = pathVariable.value();
        Map<String, String> urlParameterMappings = methodDetail.getUrlParameterMappings();
        String requestParameterValue = urlParameterMappings.get(requestParameter);
        return ObjectUtil.convert(parameter.getType(), requestParameterValue);
    }

}
