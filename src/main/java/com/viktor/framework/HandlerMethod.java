package com.viktor.framework;


import lombok.Value;

import java.lang.reflect.Method;

@Value
public class HandlerMethod {
  Object handler;
  Method method;
}
