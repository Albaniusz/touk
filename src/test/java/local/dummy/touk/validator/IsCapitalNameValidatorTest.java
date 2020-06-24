package local.dummy.touk.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsCapitalNameValidatorTest {
    private IsCapitalNameValidator isCapitalNameValidator = new IsCapitalNameValidator();

    @Test
    public void givenName_valid() {
        assertTrue(isCapitalNameValidator.isValid("Lorem", null));
    }

    @Test
    public void givenName_PolishLetters() {
        assertTrue(isCapitalNameValidator.isValid("Łoółźęrem", null));
    }

    @Test
    public void givenName_unvalid_noCapitalLetter() {
        assertFalse(isCapitalNameValidator.isValid("lorem", null));
    }

    @Test
    public void givenName_unvalidTwoWords() {
        assertFalse(isCapitalNameValidator.isValid("Lorem ipsum", null));
    }

    @Test
    public void givenName_Empty() {
        assertFalse(isCapitalNameValidator.isValid("", null));
    }

    @Test
    public void givenName_unallowedSigns() {
        assertFalse(isCapitalNameValidator.isValid("Lor123!@em", null));
    }
}
