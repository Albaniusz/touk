package local.dummy.touk.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsCapitalSurnameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsCapitalSurname {
	String message() default "Incorret name format, use: Abcd Efgh";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
