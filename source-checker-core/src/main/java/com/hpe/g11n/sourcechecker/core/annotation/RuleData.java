package com.hpe.g11n.sourcechecker.core.annotation;



import com.hpe.g11n.sourcechecker.core.IRule;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RuleData {
    String id();
    String name();
    int order();
    Class<? extends IRule> ruleClass();
    String description() default "";
}
