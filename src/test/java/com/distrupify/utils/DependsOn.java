package com.distrupify.utils;

import java.lang.annotation.*;

/**
 * Labels the method that dependent tests should be passed before running the method
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface DependsOn {
    String[] value() default "";
}
