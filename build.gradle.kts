import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

buildscript {
    repositories {
        mavenLocal()
    }
}

plugins {
    java
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.github.viclovsky"
version = version

val root = rootProject.projectDir
val gradleScriptDir by extra("$root/gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

configure(subprojects) {
    group = "com.github.viclovsky"
    version = version

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")
    apply(plugin = "java-library")

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("com.fasterxml.jackson:jackson-bom:2.15.3")
        }
        dependencies {
            dependency("org.freemarker:freemarker:2.3.31")

            //swagger 2.x
            dependency("io.swagger:swagger-models:1.6.12")
            //swagger 3.x
            dependency("io.swagger.core.v3:swagger-core:2.2.19")
            dependency("io.swagger.core.v3:swagger-models:2.2.19")
            dependency("io.swagger.parser.v3:swagger-parser:2.1.19")

            dependency("org.slf4j:slf4j-api:1.7.32")
            dependency("ch.qos.logback:logback-classic:1.2.10")

            dependency("com.beust:jcommander:1.81")
            dependency("junit:junit:4.13.2")
            dependency("org.hamcrest:hamcrest:2.2")
            dependency("com.jayway.jsonpath:json-path-assert:2.7.0")
            dependency("io.rest-assured:rest-assured:4.4.0")
            dependency("com.github.tomakehurst:wiremock:2.27.2")

            dependency("org.springframework:spring-web:5.3.7")

            dependency("com.intuit.karate:karate-core:1.4.1")
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    val sourceJar by tasks.creating(Jar::class) {
        from(sourceSets.getByName("main").allSource)
        archiveClassifier.set("sources")
    }

    val javadocJar by tasks.creating(Jar::class) {
        from(tasks.getByName("javadoc"))
        archiveClassifier.set("javadoc")
    }

    tasks.withType(Javadoc::class) {
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }

    artifacts.add("archives", sourceJar)
    artifacts.add("archives", javadocJar)


    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.compileTestJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
    }

    tasks.jar {
        manifest {
            attributes(mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version

            ))
        }
    }

    apply(from = "$gradleScriptDir/maven-publish.gradle")
}

repositories {
    mavenCentral()
}
