package com.batiaev.aiml.utils;

import java.util.List;
import java.util.Random;

/**
 * Created by anton on 21/10/16.
 *
 * @author anton
 */
public class AppUtils {
    private static Random random = new Random();

    public static <E> E getRandom(List<E> collection) {
        return collection.get(random.nextInt(collection.size()));
    }
}
