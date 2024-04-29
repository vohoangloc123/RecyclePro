plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.recyclepro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recyclepro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //dynamoDB
    implementation("com.amazonaws:aws-android-sdk-core:2.75.0")
    implementation("com.amazonaws:aws-android-sdk-ddb:2.75.0")
    implementation("com.amazonaws:aws-android-sdk-ddb-document:2.4.5")
    implementation("com.amazonaws:aws-android-sdk-s3:2.75.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}