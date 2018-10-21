package com.batiaev.aiml.loaders;

import com.batiaev.aiml.entity.AimlMap;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Map loader
 *
 * @author antonF
 * @since 19/10/16
 */
public class MapLoader<T extends AimlMap> implements FileLoader<T> {
    private static final Logger log = getLogger(MapLoader.class);

    @Override
    public T load(File file) {

        if (file == null) {
            log.error("File is null");
            return null;
        }
        if (!file.exists()) {
            log.error("File {} is not exist", file.getAbsolutePath());
            return null;
        }

        final AimlMap data = new AimlMap(file.getName(), loadFile(file));

        log.info("Loaded {} records from {}", data.size(), file.getName());
        return (T) data;
    }

    @Override
    public Map<String, T> loadAll(File... files) {
        Map<String, T> data = new HashMap<>();
        for (File file : files)
            data.put(file.getName(), load(file));
        log.info("Loaded {} files", data.size());
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
