//import gradle.kotlin.dsl.accessors._fb42485b7059bdab26943ea3b4b3710e.testImplementation

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")

dependencies {
    testImplementation(libs.findLibrary("junit").get())
    testImplementation(libs.findLibrary("coroutines-test").get())
    testImplementation(libs.findLibrary("mockk").get())
    testImplementation(libs.findLibrary("assertJTest").get())
}