package com.batiaev.aiml.loaders;

import com.batiaev.aiml.entity.AIMLSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anton on 19/10/16.
 *
 * @author anton
 */
public class SetLoader implements Loader<AIMLSet> {
    private static final Logger LOG = LoggerFactory.getLogger(SetLoader.class);

    @Override
    public AIMLSet load(File file) {

        if (file == null) {
            LOG.error("File is null");
            return null;
        }
        if (!file.exists()) {
            LOG.error("File {} is not exist", file.getAbsolutePath());
            return null;
        }

        final AIMLSet data = new AIMLSet(file.getName(), loadFile(file));

        LOG.info("Loaded {} records from {}", data.size(), file.getName());
        return data;
    }

    @Override
    public Map<String, AIMLSet> loadAll(File... files) {
        Map<String, AIMLSet> data = new HashMap<>();
        for (File file : files)
            data.put(file.getName(), load(file));
        LOG.info("Loaded {} files", data.size());
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
