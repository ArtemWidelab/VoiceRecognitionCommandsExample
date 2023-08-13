import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("android-app-convention")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")


dependencies {
    implementation(libs.findLibrary("hilt-android").get())
    kapt(libs.findLibrary("kapt-hilt").get())
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}