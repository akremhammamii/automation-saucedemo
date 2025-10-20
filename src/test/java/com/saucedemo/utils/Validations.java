package com.saucedemo.utils;

import org.junit.jupiter.api.Assertions;

public class Validations {

    public void assertEquals(String expected, String actual, String message) {
        try {
            Assertions.assertEquals(expected, actual, message);
        } catch (AssertionError e) {
            throw new AssertionError(message + " - Attendu: '" + expected + "', Trouvé: '" + actual + "'");
        }
    }
    
    public void assertTrue(boolean condition, String message) {
        Assertions.assertTrue(condition, message);
    }
}