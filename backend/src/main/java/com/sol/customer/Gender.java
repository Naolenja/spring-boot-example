package com.sol.customer;
import java.util.Random;


public enum Gender {
    MALE, FEMALE;

    private static final Random PRNG = new Random();

    public static Gender randomGender()  {
        Gender[] genders = values();
        return genders[PRNG.nextInt(genders.length)];
    }
}
