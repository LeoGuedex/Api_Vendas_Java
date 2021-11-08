package com.github.com.leoguedex.vendas.validation;
import com.github.com.leoguedex.vendas.validation.constraintValidation.NotEmptyListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {

    String message() default  "A lista não pode ser vazia";

    //Sempre por na minha anotattion
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
