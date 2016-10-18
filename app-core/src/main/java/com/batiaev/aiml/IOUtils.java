package com.batiaev.aiml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author batiaev
 * Created by batyaev on 6/18/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
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
