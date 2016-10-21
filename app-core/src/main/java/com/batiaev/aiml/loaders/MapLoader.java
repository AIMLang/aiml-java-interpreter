package com.batiaev.aiml.loaders;

import com.batiaev.aiml.entity.AimlMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by anton on 19/10/16.
 *
 * @author anton
 */
public class MapLoader<T extends AimlMap> implements Loader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MapLoader.class);

    @Override
    public T load(File file) {

        if (file == null) {
            LOG.error("File is null");
            return null;
        }
        if (!file.exists()) {
            LOG.error("File {} is not exist", file.getAbsolutePath());
            return null;
        }

        final AimlMap data = new AimlMap(file.getName(), loadFile(file));

        LOG.info("Loaded {} records from {}", data.size(), file.getName());
        return (T) data;
    }

    @Override
    public Map<String, T> loadAll(File... files) {
        Map<String, T> data = new HashMap<>();
        for (File file : files)
            data.put(file.getName(), load(file));
        LOG.info("Loaded {} files", data.size());
        return data;
    }

    protected Map<String, String> loadFile(File file) {
        final Map<String, String> data = new HashMap<>();
        try (Stream<String> stream = Files.lines(file.toPath())) {
            stream.forEach(s -> parseRow(data, s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    protected void parseRow(final Map<String, String> data, final String row) {
        String[] splitStr = row.toUpperCase().trim().split(":");
        if (splitStr.length == 2)
            data.put(splitStr[0], splitStr[1]);
    }
}
