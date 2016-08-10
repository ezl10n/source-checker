package com.hpe.g11n.sourcescoring.core.annotation;



import com.hpe.g11n.sourcescoring.core.IRule;

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
