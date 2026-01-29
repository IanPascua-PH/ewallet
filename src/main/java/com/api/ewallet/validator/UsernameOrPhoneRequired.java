package com.api.ewallet.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameOrPhoneValidator.class)
@Documented
public @interface UsernameOrPhoneRequired {
    String message() default "Provide exactly one username or phoneNumber";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
