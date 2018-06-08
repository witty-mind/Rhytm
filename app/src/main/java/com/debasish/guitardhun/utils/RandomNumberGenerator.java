package com.debasish.guitardhun.utils;

import java.util.Random;

public class RandomNumberGenerator {

    public static int getRandomNumber(){
        Random rand = new Random();
        int rand_int = rand.nextInt(1000000);
        return rand_int;
    }

    public static int getRandomNumberTwenty(){
        Random rand = new Random();
        int rand_int = rand.nextInt(10);
        return rand_int;
    }
}
