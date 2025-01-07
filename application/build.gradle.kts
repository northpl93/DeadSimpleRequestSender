plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":api"))

    implementation(libs.guava)
    implementation(libs.slf4j.simple)
    implementation(libs.commons.cli)
    implementation(libs.apache.httpclient5)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "pl.north93.deadsimplerequestsender.Main"
        attributes["Add-Opens"] = "java.base/java.nio"
    }
}

tasks.test {
    useJUnitPlatform()
}