plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.pdfverse"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pdfverse"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("com.vanniktech:android-image-cropper:4.6.0")

    // Glide (latest stable as of Nov 2025)
    implementation("com.github.bumptech.glide:glide:5.0.5")
    kapt("com.github.bumptech.glide:compiler:5.0.5")

    // Apache Commons Compress
    implementation("org.apache.commons:commons-compress:1.28.0")

    // iText7 for Image → PDF (latest version 9.4.0 + sub-modules; no exclude here to avoid DSL error)
    implementation(platform("com.itextpdf:itext7-core:9.4.0"))
    implementation("com.itextpdf:kernel:9.4.0")
    implementation("com.itextpdf:layout:9.4.0")

    // Gson
    implementation("com.google.code.gson:gson:2.13.2")

    // uCrop (popular image cropper)
    implementation("com.github.yalantis:ucrop:2.2.8")

    // Force a single safe BouncyCastle version (prevents duplicate class errors)
    implementation("org.bouncycastle:bcprov-jdk18on:1.82")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.82")
}

// Global exclusion for BouncyCastle conflicts (handles iText7 + PDFBox safely)
configurations.all {
    resolutionStrategy {
        exclude(group = "org.bouncycastle")  // ← Global exclude to fix the DSL error
        force("org.bouncycastle:bcprov-jdk18on:1.78.1")
        force("org.bouncycastle:bcpkix-jdk18on:1.78.1")
    }
}


/*configurations.all {
    resolutionStrategy {
        force("org.bouncycastle:bcprov-jdk15on:1.70")
        force("org.bouncycastle:bcpkix-jdk15on:1.70")
        force("org.bouncycastle:bcutil-jdk15to18:1.70")
    }
}*/
