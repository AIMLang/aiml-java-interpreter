package com.batiaev.aiml.loaders;

import com.batiaev.aiml.entity.AimlMap;
import com.batiaev.aiml.entity.AimlSubstitution;

import java.io.File;
import java.util.Map;

/**
 * Substitution loader
 *
 * @author anton
 * @since 19/10/16
 */
public class SubstitutionLoader extends MapLoader<AimlSubstitution> {
    @Override
    public AimlSubstitution load(File file) {
        AimlMap map = super.load(file);
        return new AimlSubstitution(map.getName(), map);
    }

    @Override
    public Map<String, AimlSubstitution> loadAll(File... files) {
        return super.loadAll(files);
    }

    @Override
    protected Map<String, String> loadFile(File file) {
        return super.loadFile(file);
    }

    @Override
    protected void parseRow(final Map<String, String> data, final String row) {
        String[] splitStr = row.toUpperCase().trim().split(",");
        if (splitStr.length < 2) return;
        String first = splitStr[0];
        String second = splitStr[1];
        if (first.length() >= 2 && second.length() >= 2) data.put(removeBraces(first), removeBraces(second));
    }

    private String removeBraces(String value) {
        return value.substring(1, value.length() - 2).trim();
    }
}
