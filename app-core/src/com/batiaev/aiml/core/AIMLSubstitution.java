package com.batiaev.aiml.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;

/**
 * @author batiaev
 * implements AIML Map
 *
 * A map is a function from one string set to another.
 * Elements of the domain are called keys and elements of the range are called values.
 *
 */
public class AIMLSubstitution extends HashMap<String, String> {
    public String  susbstitutionName;
    private static final Logger LOG = LogManager.getLogger(AIMLSubstitution.class);

    public AIMLSubstitution() {
    }

    public AIMLSubstitution(String path) {
        loadFile(path);
    }

    public void loadFile(String path) {
        File file = new File(path);
        susbstitutionName = file.getName();

        if (!file.exists()) {
            LOG.warn(path + " not found");
            return;
        }

        try {
            FileInputStream fstream = new FileInputStream(path);
            int cnt = loadFromInputStream(fstream);
            LOG.debug("Load substitution \t\t" + susbstitutionName + " [" + cnt + "]");
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
                String[] splitStr = strLine.split(",");
                String first = splitStr[0];
                String second = splitStr[1];
                if (first.length() >= 2 && second.length() >= 2) put(removeBraces(first), removeBraces(second));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cnt;
    }

    private String removeBraces(String value) {
        return value.substring(1, value.length() - 2).trim();
    }
}
