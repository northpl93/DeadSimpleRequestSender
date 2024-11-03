plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    shadow(project(":api"))

    implementation(platform(libs.google.cloud.libraries.bom))
    implementation(libs.google.cloud.bigquerystorage)

    implementation(libs.arrow.vector)
    implementation(libs.arrow.memory.netty)
}

configurations {
    runtimeClasspath {
        exclude("org.slf4j", "slf4j-api")
        exclude("com.fasterxml.jackson.core", "jackson-core")
        exclude("com.fasterxml.jackson.core", "jackson-annotations")
        exclude("com.fasterxml.jackson.core", "jackson-databind")
        exclude("com.fasterxml.jackson.core", "jackson-datatype-jsr310")
    }
}