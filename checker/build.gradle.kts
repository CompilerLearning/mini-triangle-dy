plugins {
    kotlin("jvm")
}

group = "com.improve777.triangle-checker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ast"))
    implementation(kotlin("stdlib"))
    testImplementation(Libs.Junit.api)
    testImplementation(Libs.Matcher.truth)
    testImplementation(project(":scanner"))
    testImplementation(project(":parser"))
    testRuntimeOnly(Libs.Junit.engine)
}