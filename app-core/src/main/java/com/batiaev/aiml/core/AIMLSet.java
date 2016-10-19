package com.batiaev.aiml.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;

/**
 * Implements AIML Sets
 *
 * @author anbat
 */
public class AIMLSet extends HashSet<String> {
    public String setName;
    private static final Logger LOG = LoggerFactory.getLogger(AIMLSet.class);

    public AIMLSet() {
    }

    public AIMLSet(String path) {
        loadFile(path);
    }

    void loadFile(String path) {
        File file = new File(path);
        setName = file.getName();

        if (!file.exists()) {
            LOG.warn(path + " not found");
            return;
        }

        try {
            FileInputStream fstream = new FileInputStream(path);
            int cnt = loadFromInputStream(fstream);
            LOG.debug("Load set \t\t" + setName + " [" + cnt + "]");
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
            while ((strLine = br.readLine()) != null && strLine.length() > 0) {
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
