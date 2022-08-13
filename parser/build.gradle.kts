plugins {
    kotlin("jvm")
}

group = "com.improve777.triangle-parser"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(Libs.Junit.api)
    testImplementation(Libs.Junit.engine)
}