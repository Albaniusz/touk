package local.dummy.touk.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsCapitalNameValidator implements ConstraintValidator<IsCapitalName, String> {
	@Override
	public void initialize(IsCapitalName constraintAnnotation) {

	}

	/**
	 * Validate value must be: Abcdef
	 *
	 * @param value   string to validate
	 * @param context
	 * @return true is it's valid, false, if it isn't
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && value.matches("^([A-ZĘÓĄŚŁŻŹĆŃ]{1}[A-Za-zĘÓĄŚŁŻŹĆŃęóąśłżźćń]*)$");
	}
}
