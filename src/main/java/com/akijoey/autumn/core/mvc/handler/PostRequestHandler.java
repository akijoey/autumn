package com.akijoey.autumn.core.mvc.handler;

import com.akijoey.autumn.core.ioc.BeanFactory;
import com.akijoey.autumn.core.ioc.BeanHelper;
import com.akijoey.autumn.core.mvc.entity.MethodDetail;
import com.akijoey.autumn.core.mvc.factory.FullHttpResponseFactory;
import com.akijoey.autumn.core.mvc.factory.ParameterResolverFactory;
import com.akijoey.autumn.core.mvc.factory.RouteMethodMapper;
import com.akijoey.autumn.core.mvc.resolver.ParameterResolver;
import com.akijoey.autumn.core.mvc.util.UrlUtil;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class PostRequestHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(PostRequestHandler.class);

    @Override
    public FullHttpResponse handle(FullHttpRequest fullHttpRequest) {
        String requestUri = fullHttpRequest.uri();
        // get http request path，such as "/user"
        String requestPath = UrlUtil.getRequestPath(requestUri);
        // get target method
        MethodDetail methodDetail = RouteMethodMapper.getMethodDetail(requestPath, HttpMethod.POST);
        methodDetail.setHeaderParameterMappings(fullHttpRequest.headers());
        Method targetMethod = methodDetail.getMethod();
        if (targetMethod == null) {
            return null;
        }
        String contentType = this.getContentType(fullHttpRequest.headers());
        // target method parameters.
        // notice! you should convert it to array when pass into the executeMethod()
        List<Object> targetMethodParams = new ArrayList<>();
        if (contentType.equals("application/json")) {
            String json = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            methodDetail.setJson(json);
            Parameter[] targetMethodParameters = targetMethod.getParameters();
            for (Parameter parameter : targetMethodParameters) {
                ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
                if (parameterResolver != null) {
                    Object param = parameterResolver.resolve(methodDetail, parameter);
                    targetMethodParams.add(param);
                }
            }
        } else {
            throw new IllegalArgumentException("only receive application/json type data");
        }
        String beanName = BeanHelper.getBeanName(methodDetail.getMethod().getDeclaringClass());
        Object targetObject = BeanFactory.BEANS.get(beanName);
        return FullHttpResponseFactory.getSuccessResponse(targetMethod, targetMethodParams, targetObject);
    }

    private String getContentType(HttpHeaders headers) {
        String typeStr = headers.get("Content-Type");
        String[] list = typeStr.split(";");
        return list[0];
    }

}


