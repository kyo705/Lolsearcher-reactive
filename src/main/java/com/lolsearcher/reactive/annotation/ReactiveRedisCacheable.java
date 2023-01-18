package com.lolsearcher.reactive.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReactiveRedisCacheable {

    String key() default "";

    String ttl() default "60"; /* 1min */
}
