package cn.yrm.tools.common.annotation;

/**
 * @author yuanr
 */
public @interface SimpleExcel {

    String name();

    String dictCode() default "";

    String format() default "";

    double width() default 15.0;

    int orderNum() default 0;
}
