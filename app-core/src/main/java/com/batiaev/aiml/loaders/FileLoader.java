package com.batiaev.aiml.loaders;

import java.io.File;
import java.util.Map;

/**
 * File loader
 * Created by anton on 19/10/16.
 *
 * @param <T> type of result data
 * @author anton
 */
public interface FileLoader<T> extends Loader<T, File> {
    T load(File file);

    Map<String, T> loadAll(File... files);
}
