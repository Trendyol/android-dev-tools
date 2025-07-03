plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    `maven-publish`
}

android {
    compileSdk = 34
    namespace = "com.trendyol.android.devtools.autofillservice"

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = false
        viewBinding = true
    }

    publishing {
        singleVariant("release")
    }
}

extra.apply {
    set("PUBLISH_GROUP_ID", "com.trendyol.android.devtools")
    set("PUBLISH_VERSION", "0.2.0")
    set("PUBLISH_ARTIFACT_ID", "autofill-service")
    set("PUBLISH_DESCRIPTION", "Android QA Form Autofill Service")
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
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.jetbrains.kotlin.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.squareup.moshi.kotlin)
}
