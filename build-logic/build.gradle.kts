plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()

    gradlePluginPortal()
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")

dependencies {
    api(libs.findLibrary("gradlePlugin-kotlin").get())
    api(libs.findLibrary("androidToolsBuildGradle").get())
    api(libs.findLibrary("gradlePlugin-hilt").get())
}