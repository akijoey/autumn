package com.akijoey.autumn.core.mvc.resolver;

import com.akijoey.autumn.annotation.mvc.RequestHeader;
import com.akijoey.autumn.common.util.ObjectUtil;
import com.akijoey.autumn.core.mvc.entity.MethodDetail;

import io.netty.handler.codec.http.HttpHeaders;

import java.lang.reflect.Parameter;

/**
 * process @RequestHeader annotation
 *
 **/
public class RequestHeaderParameterResolver implements ParameterResolver {

    @Override
    public Object resolve(MethodDetail methodDetail, Parameter parameter) {
        RequestHeader requestHeader = parameter.getDeclaredAnnotation(RequestHeader.class);
        String requestParameter = requestHeader.value();
        HttpHeaders headerParameterMapping = methodDetail.getHeaderParameterMappings();
        String headerParameterValue = headerParameterMapping.get(requestParameter);
        return ObjectUtil.convert(parameter.getType(), headerParameterValue);
    }

}
