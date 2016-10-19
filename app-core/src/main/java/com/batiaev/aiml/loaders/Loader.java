package com.batiaev.aiml.loaders;

import java.io.File;
import java.util.Map;

/**
 * Created by anton on 19/10/16.
 *
 * @author anton
 */
public interface Loader<T> {
    T load(File file);

    Map<String, T> loadAll(File... files);
}
