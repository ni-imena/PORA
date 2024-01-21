import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.virtualrunner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.virtualrunner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "META-INF/native-image/org.mongodb/bson/native-image.properties"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("io.github.serpro69:kotlin-faker:1.15.0")
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("org.osmdroid:osmdroid-android:6.1.17")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
    implementation("io.realm.kotlin:library-base:1.11.0")
    implementation("io.realm.kotlin:library-sync:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    implementation ("org.mongodb:mongodb-driver-sync:4.4.0")
    implementation ("org.mongodb:mongodb-driver-reactivestreams:4.11.0")
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")


    //implementation ("com.github.hannesa2:paho.mqtt.android:Tag")

}