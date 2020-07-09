package com.ky.ykt.logUtil;

import java.lang.annotation.*;

/**
 * @ClassName Log
 * @Description: 日志接口
 * @Author czw
 * @Date 2020/2/18
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     *
     * @return
     */
    String module() default "";

    /**
     * 描述
     *
     * @return
     */
    String description() default "";
}
