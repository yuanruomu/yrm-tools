package cn.yrm.tools.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuanr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SimpleExcel {

    String name();

    String dictCode() default "";

    String format() default "";

    double width() default 15.0;

    double height() default 10.0;

    int orderNum() default 0;

    Cell type() default Cell.STRING;
}
