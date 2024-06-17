plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.example.licenta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.licenta"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage:20.1.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")

    implementation("org.apache.poi:poi:3.9")
    implementation ("org.apache.poi:poi-ooxml:3.9")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

}
