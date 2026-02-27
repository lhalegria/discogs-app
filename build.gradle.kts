import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure(DetektExtension::class.java) {
            buildUponDefaultConfig = true
            config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        }
    }
}

tasks.register<Exec>("installGitHooks") {
    group = "build setup"
    description = "Configures Git to use the repository hook scripts."

    workingDir = rootDir
    commandLine("git", "config", "core.hooksPath", ".githooks")
}
