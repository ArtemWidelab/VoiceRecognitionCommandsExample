plugins {
    id("android-hilt-library-convention")
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")

dependencies {
    implementation(libs.findLibrary("lifecycle-runtime-ktx").get())
    implementation(libs.findLibrary("lifecycle-viewmodel").get())
    api(libs.findLibrary("immutableCollections").get())
    kapt(libs.findLibrary("kapt-androidLifecycleCompiler").get())
}