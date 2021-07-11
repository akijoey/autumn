package com.akijoey.autumn.core.mvc.resolver;

import com.akijoey.autumn.annotation.mvc.RequestParam;
import com.akijoey.autumn.common.util.ObjectUtil;
import com.akijoey.autumn.core.mvc.entity.MethodDetail;

import java.lang.reflect.Parameter;

/**
 * process @RequestParam annotation
 *
 **/
public class RequestParamParameterResolver implements ParameterResolver {

    @Override
    public Object resolve(MethodDetail methodDetail, Parameter parameter) {
        RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
        String requestParameter = requestParam.value();
        String requestParameterValue = methodDetail.getQueryParameterMappings().get(requestParameter);
        if (requestParameterValue == null) {
            if (requestParam.require() && requestParam.defaultValue().isEmpty()) {
                throw new IllegalArgumentException("The specified parameter " + requestParameter + " can not be null!");
            } else {
                requestParameterValue = requestParam.defaultValue();
            }
        }
        // convert the parameter to the specified type
        return ObjectUtil.convert(parameter.getType(), requestParameterValue);
    }

}
