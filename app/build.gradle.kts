plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.iptv_mobile"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.iptv_mobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        // Removed compose compiler args for metrics and reports due to file path issues on Windows.
        // freeCompilerArgs += listOf(
        //     "-P",
        //     "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$project.buildDir/compose_metrics"
        // )
        // freeCompilerArgs += listOf(
        //     "-P",
        //     "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$project.buildDir/compose_reports"
        // )
    }
      buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.3.1")
    releaseImplementation("io.objectbox:objectbox-android:4.3.1")
}

apply(plugin = "io.objectbox")