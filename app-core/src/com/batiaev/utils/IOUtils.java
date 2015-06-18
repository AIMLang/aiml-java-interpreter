package com.batiaev.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by batyaev on 6/18/15.
 */
public class IOUtils {
    public static String read() {
        BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
        String textLine = null;
        try {
            textLine = lineOfText.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textLine;
    }

    public static void write(String message) {
        System.out.println(message);
    }
}
