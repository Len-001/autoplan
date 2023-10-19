package com.github.task.base.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskAction {

    /**
     * 子任务名称 用于任务结果日志回显
     */
    String name();

    /**
     * 子任务延时
     */
    int delay() default 0;

    /**
     * 子任务执行顺序，越大就越晚执行
     */
    int order() default 0;

}
