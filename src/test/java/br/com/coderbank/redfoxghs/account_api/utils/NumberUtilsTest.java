package br.com.coderbank.redfoxghs.account_api.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberUtilsTest {

    @Test
    public void testGenerateRandomNumberBetween_ValidRange() {
        int randomNumber = NumberUtils.generateRandomNumberBetween(1, 10);
        assertTrue(randomNumber >= 1 && randomNumber <= 10);
    }

    @Test
    public void testGenerateRandomNumberBetween_InvalidRange() {
        String expectedMessage = "O valor mínimo deve ser menor que o valor máximo.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            NumberUtils.generateRandomNumberBetween(10, 1);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testGenerateRandomNumberWithDigits_ValidNumberOfDigits() {
        int randomNumber = NumberUtils.generateRandomNumberWithDigits(3);
        assertTrue(randomNumber >= 100 && randomNumber <= 999);
    }

    @Test
    public void testGenerateRandomNumberWithDigits_InvalidNumberOfDigits() {
        String expectedMessage = "O número de dígitos deve ser maior que zero.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            NumberUtils.generateRandomNumberWithDigits(0);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }
}
