plugins {
    id("android-hilt-library-convention")
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.findVersion("composeKotlinCompiler").get().requiredVersion
    }
}

dependencies {
    implementation(platform(libs.findLibrary("compose-bom").get()))
    implementation(libs.findLibrary("compose-activity").get())
    implementation(libs.findLibrary("compose-ui").get())
    implementation(libs.findLibrary("compose-ui-graphics").get())
    implementation(libs.findLibrary("compose-ui-tooling-preview").get())
    implementation(libs.findLibrary("compose-material3").get())
    implementation(libs.findLibrary("lifecycle-composeviewmodel").get())
}