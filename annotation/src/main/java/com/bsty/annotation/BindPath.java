package com.bsty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //声明注解的作用于，其实就是放在什么上面
@Retention(RetentionPolicy.CLASS) //声明注解的生命周期
public @interface BindPath {
    String value();
}
