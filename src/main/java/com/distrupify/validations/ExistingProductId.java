package com.distrupify.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingProductIdValidator.class})
public @interface ExistingProductId {
    String message() default "Product id invalid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    boolean token() default false;
}
