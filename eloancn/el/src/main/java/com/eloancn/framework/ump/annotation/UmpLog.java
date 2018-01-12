package com.eloancn.framework.ump.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/4/6.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UmpLog {
    LogType logType() default LogType.Transaction;

    enum MethodType {
        Public, All
    }

    /**
     * Transaction Parameter type;
     *
     * @return
     */
    String type() default "";

    /**
     * Transaction Parameter name;
     *
     * @return
     */
    String name() default "";

    /**
     * is add monitor log or not .
     *
     * @return
     */
    boolean log() default true;

    MethodType logMethodType() default MethodType.Public;

}
