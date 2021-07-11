package com.akijoey.autumn.core.config;

import com.akijoey.autumn.core.config.resource.ResourceLoader;
import com.akijoey.autumn.core.config.resource.property.PropertiesResourceLoader;
import com.akijoey.autumn.core.config.resource.yaml.YamlResourceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigurationManager implements Configuration {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);

    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private static final String YAML_FILE_EXTENSION = ".yaml";
    private static final String YML_FILE_EXTENSION = ".yml";

    private final Configuration configuration;

    public ConfigurationManager(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int getInt(String id) {
        return configuration.getInt(id);
    }

    @Override
    public String getString(String id) {
        return configuration.getString(id);
    }

    @Override
    public Boolean getBoolean(String id) {
        return configuration.getBoolean(id);
    }

    @Override
    public void loadResources(List<Path> resourcePaths) {
        try {
            for (Path resourcePath : resourcePaths) {
                String fileName = resourcePath.getFileName().toString();
                if (fileName.endsWith(PROPERTIES_FILE_EXTENSION)) {
                    ResourceLoader resourceLoader = new PropertiesResourceLoader();
                    configuration.putAll(resourceLoader.loadResource(resourcePath));
                } else if (fileName.endsWith(YML_FILE_EXTENSION) || fileName.endsWith(YAML_FILE_EXTENSION)) {
                    ResourceLoader resourceLoader = new YamlResourceLoader();
                    configuration.putAll(resourceLoader.loadResource(resourcePath));
                }
            }
        } catch (IOException ex) {
            log.error("not load resources.");
            System.exit(-1);
        }
    }

}
