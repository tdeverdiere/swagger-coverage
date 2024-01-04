package com.github.viclovsky.swagger.coverage.karate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants;
import com.github.viclovsky.swagger.coverage.core.generator.Generator;
import com.intuit.karate.FileUtils;
import com.intuit.karate.Logger;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.intuit.karate.core.MockServer;

public class SwaggerCoverageRunner extends Runner {
    private static final Logger logger = new Logger();

    private static MockServer mockServer;

    private static int startProxy(Map<String, Object> args){
        mockServer = MockServer
                    .feature("classpath:httpProxy.feature")
                    .args(args)
                    .http(0).build();

        return mockServer.getPort();
    }

    private static void stopProxy(){
        if (mockServer != null){
            mockServer.stop();
        }
    }

    public static class SwaggerCoverageBuilder extends Builder<SwaggerCoverageBuilder> {
        final String SPECIFICATION_NAME = "swagger-specification";
        final String CONFIG_NAME = "swagger-coverage-config.json";

        boolean oas3;
        URI specificationPath;
        String configPath;
        String inputPath;
        String coverageDir;
        boolean backupCoverageOutput;

        private void prepareTests(){
            if(backupCoverageOutput){
                backupCoverageOutput();
            }

            File outputDir = new File(coverageDir, SwaggerCoverageConstants.OUTPUT_DIRECTORY);
            if (outputDir.exists()) FileUtils.deleteDirectory(outputDir);

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("oas3", oas3);
            args.put("workingDir", coverageDir);
            int proxyPort = startProxy(args);
            systemProperty("proxy.port", proxyPort + "");
            logger.info("Started proxy at port: {}", proxyPort);
        }

        private void backupCoverageOutput(){
            File file = new File(coverageDir, SwaggerCoverageConstants.OUTPUT_DIRECTORY);
            if (file.exists()) {
                File newDir = new File(coverageDir, SwaggerCoverageConstants.OUTPUT_DIRECTORY + "_" + System.currentTimeMillis());
                if (file.renameTo(newDir)){
                    logger.info("backed up existing swagger-coverage-output dir to: {}", newDir);
                } else {
                    logger.warn("failed to backup existing swagger-coverage-output dir: {}", file);
                }
            }
        }

        private void generateReport() {
            if (coverageDir == null) coverageDir = "";

            Path inputPath = Paths.get(coverageDir, SwaggerCoverageConstants.OUTPUT_DIRECTORY);

            if (!inputPath.toFile().exists()){
                try {
                    Files.createDirectory(inputPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn("Failed to create empty directory for coverage input: {}. Report could not be generated.", inputPath);
                    return;
                }
            }

            Generator generator = new Generator()
                .setInputPath(inputPath);

            if (specificationPath != null) {
                generator.setSpecPath(specificationPath);
            }
            else{
                File specFile = Optional.of(Paths.get(coverageDir, SPECIFICATION_NAME + ".json").toFile())
                .filter((file) -> file.exists())
                .orElseGet(()-> Paths.get(coverageDir, SPECIFICATION_NAME + ".yaml").toFile());

                if (!specFile.exists()){
                    throw new NoSuchElementException();
                }

                generator.setSpecPath(specFile.toURI());
            }

            if (configPath != null){
                generator.setConfigurationPath(Paths.get(configPath));
            }
            else{
                File configFile = Paths.get(coverageDir, CONFIG_NAME).toFile();
                if (configFile.exists()){
                    generator.setConfigurationPath(configFile.toPath());
                }
            }

            generator.run();
        }

        public SwaggerCoverageBuilder swagger(){
            oas3 = false;
            return this;
        }

        public SwaggerCoverageBuilder oas3(){
            oas3 = true;
            return this;
        }

        public SwaggerCoverageBuilder swaggerSpec(URI path){
            specificationPath = path;
            return this;
        }

        public SwaggerCoverageBuilder swaggerCoverageConfig(String configPath){
            this.configPath = configPath;
            return this;
        }

        public SwaggerCoverageBuilder coverageDir(String coverageDir){
            this.coverageDir = coverageDir;
            return this;
        }

        public SwaggerCoverageBuilder backupCoverageOutput(boolean value){
            this.backupCoverageOutput = value;
            return this;
        }

        @Override
        public Results parallel(int threadCount){
            prepareTests();
            Results results = super.parallel(threadCount);
            generateReport();
            stopProxy();
            return results;
        }

    }


    public static SwaggerCoverageBuilder path(String... paths) {
        SwaggerCoverageBuilder builder = new SwaggerCoverageBuilder();
        return builder.path(paths);
    }

    public static SwaggerCoverageBuilder path(List<String> paths) {
        SwaggerCoverageBuilder builder = new SwaggerCoverageBuilder();
        return builder.path(paths);
    }

    public static SwaggerCoverageBuilder builder() {
        return new SwaggerCoverageBuilder();
    }
}
