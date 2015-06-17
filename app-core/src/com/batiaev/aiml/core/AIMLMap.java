package com.batiaev.aiml.core;

import java.io.*;
import java.util.HashMap;

/**
    * implements AIML Map
    *
    * A map is a function from one string set to another.
    * Elements of the domain are called keys and elements of the range are called values.
    *
*/
public class AIMLMap extends HashMap<String, String> {
    public String  mapName;

    void loadFile(String path) {
        File file = new File(path);
        mapName = file.getName();

        if (!file.exists()) {
            System.out.println(path + " not found");
            return;
        }

        try {
            FileInputStream fstream = new FileInputStream(path);
            int cnt = loadFromInputStream(fstream);
            System.out.println("Load map \t\t" + mapName + " [" + cnt + "]");
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadFromInputStream(InputStream in) {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int cnt = 0;
        try {
            while ((strLine = br.readLine()) != null  && strLine.length() > 0) {
                cnt++;
                strLine = strLine.toUpperCase().trim();
                String[] splitStr = strLine.split(":");
                if (splitStr.length >= 2) put(splitStr[0], splitStr[1]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cnt;
    }
}
