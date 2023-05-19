package cn.yrm.tools.common.annotation;

import java.lang.annotation.*;

/**
 * @author xx
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Setting {
    /**
     * 关联的数据库KEY值
     *
     * @return
     */
    String value() default "";
}
