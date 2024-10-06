plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    shadow(project(":api"))

    // https://mvnrepository.com/artifact/com.esotericsoftware/kryo
    implementation("com.esotericsoftware:kryo:5.5.0")
}