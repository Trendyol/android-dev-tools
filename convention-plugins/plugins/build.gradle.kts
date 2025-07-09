plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toString().toInt())

gradlePlugin {
    plugins {
        register("publishConvention") {
            id = "com.trendyol.android.devtools.plugins.publish"
            implementationClass = "com.trendyol.android.devtools.plugins.publish.PublishConventionPlugin"
        }
    }
}

dependencies {
    implementation(libs.gradle.maven.publish.plugin)
}
