package com.joutvhu.fixedwidth.parser.validator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixedRegular {
    String regex();

    String message() default "";

    boolean nativeMessage() default true;
}
