plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.inject/guice
    api("com.google.inject:guice:7.0.0")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    api("org.slf4j:slf4j-api:2.0.7")

    // https://mvnrepository.com/artifact/org.antlr/ST4
    api("org.antlr:ST4:4.3.4")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    api("com.fasterxml.jackson.core:jackson-core:2.15.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.0")
}