plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toString().toInt())

android {
    namespace = "com.trendyol.android.devtools.sharedprefmanager"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // aarMetadata.minCompileSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    publishing {
        singleVariant("release")
    }
}

extra.apply {
    set("PUBLISH_GROUP_ID", "com.trendyol.android.devtools")
    set("PUBLISH_VERSION", "0.2.0")
    set("PUBLISH_ARTIFACT_ID", "sharedpref-manager")
    set("PUBLISH_DESCRIPTION", "Android SharedPreferences Manager")
    set("PUBLISH_URL", "https://github.com/Trendyol/android-dev-tools")
    set("PUBLISH_LICENSE_NAME", "Android DevTools License")
    set("PUBLISH_LICENSE_URL", "https://github.com/Trendyol/android-dev-tools/blob/master/LICENSE")
    set("PUBLISH_SCM_CONNECTION", "scm:git:github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_DEV_CONNECTION", "scm:git:ssh://github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_URL", "https://github.com/Trendyol/android-dev-tools/tree/main")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.ktx)
}

apply(from = "${rootProject.rootDir}/scripts/publish-module.gradle")
