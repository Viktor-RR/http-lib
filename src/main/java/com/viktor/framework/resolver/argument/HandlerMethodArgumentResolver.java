package com.viktor.framework.resolver.argument;


import com.viktor.framework.Request;

import java.io.OutputStream;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {
  boolean supportsParameter(Parameter parameter);
  Object resolveArgument(Parameter parameter, Request request, OutputStream response);
}
