plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("androidx.room")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.atech.core"
    compileSdk = 34
    packaging.resources {
        excludes.add("META-INF/AL2.0")
        excludes.add("META-INF/LGPL2.1")
        excludes.add("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        room {
            schemaDirectory("$projectDir/schemas")
        }
    }
    flavorDimensions += "role"
    productFlavors {
        create("student") {
            dimension = "role"
            buildConfigField("boolean", "IS_ADMIN", "false")
        }
        create("teacher") {
            dimension = "role"
            buildConfigField("boolean", "IS_ADMIN", "true")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    implementation(libs.firebase.remote.config)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)


    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.gson)

    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.jsoup)
    implementation(libs.retrofit.json)
    implementation(libs.google.auth.library.oauth2.http)
}
hilt {
    enableAggregatingTask = true
}