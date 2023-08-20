plugins {
    id("compose-convention")
}

android {
    namespace = "ua.widelab.main_commands.compose"
}

dependencies {
    implementation(project(":main-commands:presentation"))
    implementation(libs.accompanist.permissions)
}