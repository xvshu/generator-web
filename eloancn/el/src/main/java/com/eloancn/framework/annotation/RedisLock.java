package com.eloancn.framework.annotation;

import java.lang.annotation.*;

/**
 * 使用注解方式实现redis分布式锁操作的设计
 * Created by zero on 14-4-8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    //redis的key
    String lockName() default "redisKey";

    //最长等待时间
    long maxWait() default 1000*30;

    //键的过期时间
    long expiredTime() default 1000*30;
}
