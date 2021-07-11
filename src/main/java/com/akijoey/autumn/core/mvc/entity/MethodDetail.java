package com.akijoey.autumn.core.mvc.entity;

import io.netty.handler.codec.http.HttpHeaders;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MethodDetail {

    public MethodDetail() {
    }

    // target method
    private Method method;
    // url parameter mapping
    private Map<String, String> urlParameterMappings;
    // url query parameter mapping
    private Map<String, String> queryParameterMappings;
    // json type http post request data
    private String json;
    // request header data
    private HttpHeaders headerParameterMappings;

    public Method getMethod() {
        return method;
    }
    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getUrlParameterMappings() {
        return urlParameterMappings;
    }
    public void setUrlParameterMappings(Map<String, String> urlParameterMappings) {
        this.urlParameterMappings = urlParameterMappings;
    }

    public Map<String, String> getQueryParameterMappings() {
        return queryParameterMappings;
    }
    public void setQueryParameterMappings(Map<String, String> queryParameterMappings) {
        this.queryParameterMappings = queryParameterMappings;
    }

    public String getJson() {
        return json;
    }
    public void setJson(String json) {
        this.json = json;
    }

    public HttpHeaders getHeaderParameterMappings() {
        return headerParameterMappings;
    }
    public void setHeaderParameterMappings(HttpHeaders headerParameterMappings) {
        this.headerParameterMappings = headerParameterMappings;
    }


    public void build(String requestPath, Map<String, Method> requestMappings, Map<String, String> urlMappings) {
        requestMappings.forEach((key, value) -> {
            Pattern pattern = Pattern.compile(key);
            boolean b = pattern.matcher(requestPath).find();
            if (b) {
                this.setMethod(value);
                String url = urlMappings.get(key);
                Map<String, String> urlParameterMappings = getUrlParameterMappings(requestPath, url);
                this.setUrlParameterMappings(urlParameterMappings);
            }
        });
    }

    /**
     * Match the request path parameter to the URL parameter
     * <p>
     * eg: requestPath="/user/1" url="/user/{id}"
     * this method will return:{"id" -> "1","user" -> "user"}
     * </p>
     */
    private Map<String, String> getUrlParameterMappings(String requestPath, String url) {
        String[] requestParams = requestPath.split("/");
        String[] urlParams = url.split("/");
        Map<String, String> urlParameterMappings = new HashMap<>();
        for (int i = 1; i < urlParams.length; i++) {
            urlParameterMappings.put(urlParams[i].replace("{", "").replace("}", ""), requestParams[i]);
        }
        return urlParameterMappings;
    }

}
