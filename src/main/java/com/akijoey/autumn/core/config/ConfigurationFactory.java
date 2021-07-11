package com.akijoey.autumn.core.config;

public class ConfigurationFactory {

    public static Configuration getConfig() {
        return SingleConfigurationHolder.INSTANCE;
    }

    private static class SingleConfigurationHolder {

        private static final Configuration INSTANCE = buildConfiguration();

        private static Configuration buildConfiguration() {
            return new DefaultConfiguration();
        }
    }

}
