plugins {
    id("android-hilt-library-convention")
}

android {
    namespace = "ua.widelab.main_commands.repo"
}

dependencies {
    api(project(":main-commands:entities"))
    implementation(libs.immutableCollections)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.assertJTest)
    testImplementation(libs.coroutines.test)
}