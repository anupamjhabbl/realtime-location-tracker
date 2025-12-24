plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.example.location_tracking_core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.gson)
    implementation(libs.hilt.android)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.google.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}



