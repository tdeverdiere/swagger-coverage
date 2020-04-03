package com.github.viclovsky.swagger.coverage.config;

import java.nio.file.Path;

public class Configuration {

    private Path specPath;
    private Path inputPath;

    private Configuration() {
    }

    public static Configuration conf() {
        return new Configuration();
    }

    public Configuration setSpecPath(Path specPath) {
        this.specPath = specPath;
        return this;
    }

    public Configuration setOutputPath(Path reqPath) {
        this.inputPath = reqPath;
        return this;
    }

    public Path getSpecPath() {
        return specPath;
    }

    public Path getOutputPath() {
        return inputPath;
    }
}
