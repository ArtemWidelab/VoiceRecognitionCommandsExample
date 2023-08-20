pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VoiceRecognitionCommandsExample"
include(":app")
includeBuild("build-logic")
include(":android-initializable")
include(":logger")
include(":main-commands:entities")
include(":main-commands:repo")
include(":audio-recording")