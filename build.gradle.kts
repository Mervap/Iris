buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.6.21"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.12.0")
        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:0.12.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}