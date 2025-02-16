plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":api"))
    shadow(project(":api"))

    implementation(libs.jsonschema.generator)
    implementation(libs.jsonschema.generator.jackson)
    implementation(libs.javalin)
}