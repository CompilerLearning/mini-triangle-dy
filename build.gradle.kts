plugins {
    kotlin("jvm") version Libs.Kotlin.version apply false
}

allprojects {
    group = "com.improve777.triangle"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
}