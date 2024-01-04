plugins {
    java
    `java-library`
}

description = "Swagger Coverage Karate"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":swagger-coverage-commons"))
    api(project(":swagger-coverage-commandline"))
    implementation("io.swagger:swagger-models")
    implementation("io.swagger.core.v3:swagger-models")
    implementation("com.intuit.karate:karate-core")

    //needed for karate runner
    implementation("com.linecorp.armeria:armeria:1.26.4")
    implementation("io.netty:netty-all:4.1.104.Final")
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("io.github.classgraph:classgraph:4.8.165")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    testImplementation("junit:junit")
    testImplementation("org.hamcrest:hamcrest")
    testImplementation("com.github.tomakehurst:wiremock")
}

tasks {
    test {
        workingDir(buildDir)
    }
}
