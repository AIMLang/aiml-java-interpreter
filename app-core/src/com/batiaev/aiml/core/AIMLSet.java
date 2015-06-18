package com.batiaev.aiml.core;

import java.io.*;
import java.util.HashSet;

/**
 * implements AIML Sets
 */
public class AIMLSet extends HashSet<String> {
    public String setName;

    public AIMLSet() {
    }

    public AIMLSet(String path) {
        loadFile(path);
    }

    void loadFile(String path) {
        File file = new File(path);
        setName = file.getName();

        if (!file.exists()) {
            System.out.println(path + " not found");
            return;
        }

        try {
            FileInputStream fstream = new FileInputStream(path);
            int cnt = loadFromInputStream(fstream);
            System.out.println("Load set \t\t" + setName + " [" + cnt + "]");
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
                add(strLine);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cnt;
    }
}
