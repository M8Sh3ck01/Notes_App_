plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.champox.notes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.champox.notes"
        minSdk = 24
        targetSdk = 35
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
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

}

dependencies {
    // Firebase BoM to manage versions automatically
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))

    // Firebase Analytics (managed by BoM)
    implementation("com.google.firebase:firebase-analytics")

    // Optional: Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // AndroidX Lifecycle ViewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")

    // AndroidX DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Accompanist libraries for Compose
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")

    // Compose Material and Material3
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3:1.3.2")

    // Room (database)
    val roomVersion = "2.7.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // Dagger DI
    implementation("com.google.dagger:dagger:2.51.1")

    // Java 11 desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Compose UI Tooling (debug only)
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

