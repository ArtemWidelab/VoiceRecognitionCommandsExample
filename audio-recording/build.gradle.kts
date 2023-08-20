plugins {
    id("android-hilt-library-convention")
}

android {
    namespace = "ua.widelab.audio_recording"
}

dependencies {
    implementation(project(":logger"))
    implementation("org.tensorflow:tensorflow-lite-task-audio:0.4.4")
}