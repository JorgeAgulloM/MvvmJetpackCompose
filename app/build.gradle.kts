plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.softyorch.mvvmjetpackcompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.softyorch.mvvmjetpackcompose"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        resources.excludes.add("META-INF/*")
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        packaging { jniLibs { useLegacyPackaging = true } }
    }
}

dependencies {
    /** Room database library */
    val room = "2.6.1"
    implementation("androidx.room:room-runtime:$room")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")

    /** Dagger Hilt injection library */
    val dagger = "2.48"
    implementation("com.google.dagger:hilt-android:$dagger")
    kapt("com.google.dagger:hilt-android-compiler:$dagger")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    /** Coroutines library */
    val coroutines = "1.7.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    /** material icons */
    implementation("androidx.compose.material:material-icons-extended:1.6.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    /** Testing */
    val mockk = "1.13.9"
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("io.mockk:mockk:$mockk")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("io.mockk:mockk-android:$mockk")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
