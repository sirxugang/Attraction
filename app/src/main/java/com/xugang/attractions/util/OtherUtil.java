package com.xugang.attractions.util;

import java.util.Random;

/**
 * Created by ASUS on 2016-10-15.
 */
public class OtherUtil {
    public static int getRandomNumber(int scope) {
        return new Random().nextInt(scope);
    }
}
