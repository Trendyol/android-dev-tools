
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    `maven-publish`
}

android {
    namespace = "com.trendyol.android.devtools.analyticslogger"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    set("PUBLISH_ARTIFACT_ID", "analytics-logger")
    set("PUBLISH_DESCRIPTION", "Android Analytics Logger")
    set("PUBLISH_URL", "https://github.com/Trendyol/android-dev-tools")
    set("PUBLISH_LICENSE_NAME", "Android DevTools License")
    set("PUBLISH_LICENSE_URL", "https://github.com/Trendyol/android-dev-tools/blob/master/LICENSE")
    set("PUBLISH_SCM_CONNECTION", "scm:git:github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_DEV_CONNECTION", "scm:git:ssh://github.com/Trendyol/android-dev-tools.git")
    set("PUBLISH_SCM_URL", "https://github.com/Trendyol/android-dev-tools/tree/main")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.moshi)
    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

apply(from = "${rootProject.rootDir}/scripts/publish-module.gradle")
