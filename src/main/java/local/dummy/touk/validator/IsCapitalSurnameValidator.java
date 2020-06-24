package local.dummy.touk.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsCapitalSurnameValidator implements ConstraintValidator<IsCapitalSurname, String> {
	@Override
	public void initialize(IsCapitalSurname constraintAnnotation) {
	}

	/**
	 * Validate value must be: Abcd-Efgh
	 *
	 * @param value   string to validate
	 * @param context
	 * @return true is it's valid, false, if it isn't
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && value.matches("^([A-ZĘÓĄŚŁŻŹĆŃ]{1}[A-Za-zĘÓĄŚŁŻŹĆŃęóąśłżźćń]*)([-]{1}[A-ZĘÓĄŚŁŻŹĆŃ]{1}[A-Za-zĘÓĄŚŁŻŹĆŃęóąśłżźćń]*)*$");
	}
}
