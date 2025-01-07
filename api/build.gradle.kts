plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(libs.slf4j.api)
    api(libs.guice)

    api(libs.jackson.core)
    api(libs.jackson.databind)
    api(libs.jackson.dataformat.yaml)
}