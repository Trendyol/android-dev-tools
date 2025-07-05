plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    `maven-publish`
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toString().toInt())

android {
    compileSdk = 34
    namespace = "com.trendyol.android.devtools.debugmenu"

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
        // aarMetadata.minCompileSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release")
    }
}

extra.apply {
    set("PUBLISH_GROUP_ID", "com.trendyol.android.devtools")
    set("PUBLISH_VERSION", "0.2.0")
    set("PUBLISH_ARTIFACT_ID", "debug-menu")
    set("PUBLISH_DESCRIPTION", "Android Debug Menu")
    set("PUBLISH_URL", "https://github.com/Trendyol/android-dev-tools")
    set("PUBLISH_LICENSE_NAME", "Android DevTools License")
    set("PUBLISH_LICENSE_URL", "https://github.com/Trendyol/android-dev-tools/blob/master/LICENSE")
    set("PUBLISH_SCM_CONNECTION", "scm:git:github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_DEV_CONNECTION", "scm:git:ssh://github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_URL", "https://github.com/Trendyol/android-dev-tools/tree/main")
}

apply(from = "${rootProject.rootDir}/scripts/publish-module.gradle")

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.android.material)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
}
