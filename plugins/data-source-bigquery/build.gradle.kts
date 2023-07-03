plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    shadow(project(":api"))

    implementation(platform("com.google.cloud:libraries-bom:26.15.0"))
    implementation("com.google.cloud:google-cloud-bigquerystorage")

    implementation("org.apache.arrow:arrow-vector:12.0.0")
    implementation("org.apache.arrow:arrow-memory-netty:12.0.0")
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