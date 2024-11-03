plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":api"))

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.16")

    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    implementation("commons-cli:commons-cli:1.9.0")

    // https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.1")

    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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