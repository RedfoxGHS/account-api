package br.com.coderbank.redfoxghs.account_api.utils;

import java.util.Random;

public class NumberUtils {
    public static int generateRandomNumberBetween(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("O valor mínimo deve ser menor que o valor máximo.");
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static int generateRandomNumberWithDigits(int numberOfDigits) {
        if (numberOfDigits <= 0) {
            throw new IllegalArgumentException("O número de dígitos deve ser maior que zero.");
        }
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) Math.pow(10, numberOfDigits) - 1;
        return generateRandomNumberBetween(min, max);
    }
}