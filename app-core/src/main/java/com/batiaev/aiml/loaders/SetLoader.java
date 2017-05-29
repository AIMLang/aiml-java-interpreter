package com.batiaev.aiml.loaders;

import com.batiaev.aiml.entity.AimlSet;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Set loader
 *
 * @author anton
 * @since 19/10/16
 */
public class SetLoader implements FileLoader<AimlSet> {
    private static final Logger log = getLogger(SetLoader.class);

    @Override
    public AimlSet load(File file) {

        if (file == null) {
            log.error("File is null");
            return null;
        }
        if (!file.exists()) {
            log.error("File {} is not exist", file.getAbsolutePath());
            return null;
        }

        final AimlSet data = new AimlSet(file.getName(), loadFile(file));

        log.info("Loaded {} records from {}", data.size(), file.getName());
        return data;
    }

    @Override
    public Map<String, AimlSet> loadAll(File... files) {
        Map<String, AimlSet> data = new HashMap<>();
        for (File file : files)
            data.put(file.getName(), load(file));
        log.info("Loaded {} files", data.size());
        return data;
    }

    private Set<String> loadFile(File file) {
        try (Stream<String> stream = Files.lines(file.toPath())) {
            return stream.map(s -> s.toUpperCase().trim()).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }
}
