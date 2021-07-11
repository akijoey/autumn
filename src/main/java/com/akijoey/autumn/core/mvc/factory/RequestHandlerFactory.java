package com.akijoey.autumn.core.mvc.factory;

import com.akijoey.autumn.core.mvc.handler.GetRequestHandler;
import com.akijoey.autumn.core.mvc.handler.PostRequestHandler;
import com.akijoey.autumn.core.mvc.handler.RequestHandler;

import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerFactory {

    public static final Map<HttpMethod, RequestHandler> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(HttpMethod.GET, new GetRequestHandler());
        REQUEST_HANDLERS.put(HttpMethod.POST, new PostRequestHandler());
    }

    public static RequestHandler get(HttpMethod httpMethod) {
        return REQUEST_HANDLERS.get(httpMethod);
    }

}
