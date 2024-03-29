package com.lolsearcher.reactive.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReactiveRedisCacheable {

    String name() default "";

    String key() default "";

    String ttl() default "60"; /* 1min */
}
