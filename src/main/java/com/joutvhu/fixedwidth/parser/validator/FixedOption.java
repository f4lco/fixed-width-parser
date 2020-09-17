package com.joutvhu.fixedwidth.parser.validator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixedOption {
    String[] options();

    String message() default "";

    boolean nativeMessage() default true;
}
