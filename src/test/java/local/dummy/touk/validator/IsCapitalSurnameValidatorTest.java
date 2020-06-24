package local.dummy.touk.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsCapitalSurnameValidatorTest {
    private IsCapitalSurnameValidator isCapitalSurnameValidator = new IsCapitalSurnameValidator();

    @Test
    public void givenName_validOneWord() {
        assertTrue(isCapitalSurnameValidator.isValid("Lorem", null));
    }

    @Test
    public void givenName_validOneWordPolishLetters() {
        assertTrue(isCapitalSurnameValidator.isValid("Łórem", null));
    }

    @Test
    public void givenName_validTwoWords() {
        assertTrue(isCapitalSurnameValidator.isValid("Lorem-Ipsum", null));
    }

    @Test
    public void givenName_validTwoWordsPolishLetters() {
        assertTrue(isCapitalSurnameValidator.isValid("Łórem-Ipsuń", null));
    }

    @Test
    public void givenName_empty() {
        assertFalse(isCapitalSurnameValidator.isValid("", null));
    }

    @Test
    public void givenName_wrongWordSeparator() {
        assertFalse(isCapitalSurnameValidator.isValid("Lorem_ipsum", null));
    }

    @Test
    public void givenName_invalidNoCapitalLetters() {
        assertFalse(isCapitalSurnameValidator.isValid("Lorem-ipsum", null));
    }

    @Test
    public void givenName_unallowedSigns() {
        assertFalse(isCapitalSurnameValidator.isValid("Lorem-ips;um", null));
    }
}
