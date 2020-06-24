package local.dummy.touk.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsCapitalNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsCapitalName {
	String message() default "Incorret name format, use: Abcd";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
