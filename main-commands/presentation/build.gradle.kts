plugins {
    id("presentation-convention")
}

android {
    namespace = "ua.widelab.main_commands.presentation"
}

dependencies {
    implementation(project(":main-commands:repo"))
    implementation(project(":audio-recording"))
}