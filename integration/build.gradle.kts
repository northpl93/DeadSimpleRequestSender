plugins {
    id("java")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":application"))
    implementation(project(":plugins:body-factory-inline"))
    implementation(project(":plugins:data-source-buffer"))
    implementation(project(":plugins:data-source-csv"))
    implementation(project(":plugins:data-source-random"))

    implementation(platform(libs.junit.bom))
    implementation(libs.junit.jupiter)

    testImplementation(project(":api"))
    testImplementation(project(":application"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.wiremock)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}