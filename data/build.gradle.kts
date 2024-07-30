plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.secrets)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.bruno13palhano.data"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "com.bruno13palhano.data.CustomTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.mockWebServer)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.paging.test)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.coroutines.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.hilt.android)
    implementation(libs.paging.runtime)
    ksp(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.coroutines)
    implementation(libs.room.paging)
    implementation(libs.kotlinx.serialization.json)
}