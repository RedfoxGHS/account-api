package br.com.coderbank.redfoxghs.account_api.utils;

import java.util.Random;

public class NumberUtils {
    public static int generateRandomNumberBetween(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value.");
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static int generateRandomNumberWithDigits(int numberOfDigits) {
        if (numberOfDigits <= 0) {
            throw new IllegalArgumentException("Number of digits must be greater than zero.");
        }
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) Math.pow(10, numberOfDigits) - 1;
        return generateRandomNumberBetween(min, max);
    }
}