import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}
//tasks.getByPath("preBuild").dependsOn("ktlintFormat")

// ktlint {
//    android = true
//    ignoreFailures = false
//    disabledRules = ["no-wildcard-imports", "max-line-length"]
//    reporters {
//        reporter("plain")
//        reporter("checkstyle")
//        reporter("sarif")
//    }
// }

android {
    namespace = "com.marwadtech.userapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.marwadtech.userapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val HOST_API: String by project
    val STAGING_API: String by project
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "HOST", STAGING_API)
            val key: String = gradleLocalProperties(rootDir).getProperty("MAPS_API_KEY")
            resValue("string", "google_maps_api_key", key)
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("debug") {
//            applicationIdSuffix = ".debug"
            isDebuggable = true
            resValue("string", "HOST", HOST_API)
            val key: String = gradleLocalProperties(rootDir).getProperty("MAPS_API_KEY")
            resValue("string", "google_maps_api_key", key)
        }

        /**
         * The `initWith` property lets you copy configurations from other build types,
         * then configure only the settings you want to change. This one copies the debug build
         * type, and then changes the manifest placeholder and application ID.
         */
        create("staging") {
            initWith(getByName("debug"))
            manifestPlaceholders["hostName"] = "internal.example.com"
            applicationIdSuffix = ".debugStaging"
        }
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    val nav_version = "2.7.4"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    GLIDE

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")


    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // pinview
    implementation("io.github.chaosleung:pinview:1.4.4")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

//    GLIDE
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation("ru.tinkoff.scrollingpagerindicator:scrollingpagerindicator:1.2.4")

//    Swipe Refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.0.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    // one signal
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    // ImagePicker
    implementation("io.github.ParkSangGwon:tedimagepicker:1.4.2")

    // Ucrop
//    implementation("com.github.yalantis:ucrop:2.2.8")

    // compress image
    implementation("id.zelory:compressor:3.0.1")

    // Google play services
    // implementation 'com.google.android.gms:play-services:12.0.1'
    implementation("com.google.android.libraries.places:places:2.6.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")

    // Dependency to include Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")
    // [END_EXCLUDE]

    // Maps SDK for Android KTX Library
    implementation("com.google.maps.android:maps-ktx:3.3.0")

    // Maps SDK for Android Utility Library KTX Library
    implementation("com.google.maps.android:maps-utils-ktx:3.0.0")

    // map
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    /*
* MAP SDK UTILS
* */
    implementation("com.google.maps.android:android-maps-utils:2.2.0")

    // Dependency to include Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")
    // [END_EXCLUDE]

    // Maps SDK for Android KTX Library
    implementation("com.google.maps.android:maps-ktx:3.3.0")

    // Maps SDK for Android Utility Library KTX Library
    implementation("com.google.maps.android:maps-utils-ktx:3.0.0")

    // shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")

    // Pie Chart
//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")



//    implementation("androidx.core:core-splashscreen:1.0.1")
}


