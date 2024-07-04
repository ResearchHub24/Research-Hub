import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.atech.research"
    compileSdk = 34

    packaging.resources {
        excludes.add("META-INF/AL2.0")
        excludes.add("META-INF/LGPL2.1")
        excludes.add("META-INF/DEPENDENCIES")
    }
    defaultConfig {
        applicationId = "com.atech.research"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").reader())

        buildConfigField(
            "String",
            "FIREBASE_WEB_CLIENT",
            "\"${properties.getProperty("FIREBASE_WEB_CLIENT")}\""
        )
    }
    flavorDimensions += "role"
    productFlavors {
        create("student") {
            dimension = "role"
            versionNameSuffix = "-student"
        }
        create("teacher") {
            dimension = "role"
            versionNameSuffix = "-teacher"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true
    }
    composeCompiler {
        enableStrongSkippingMode = true

        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        stabilityConfigurationFile =
            rootProject.layout.projectDirectory.file("stability_config.conf")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":ui-common"))
    "studentImplementation"(project(mapOf("path" to ":app:student")))
    "teacherImplementation"(project(mapOf("path" to ":app:teacher")))
//    implementation(project(mapOf("path" to ":app:student")))
//    implementation(project(mapOf("path" to ":app:teacher")))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.ktx)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.navigation.fragment)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    implementation(libs.firebase.message)
    implementation(libs.play.service.auth)


}
hilt {
    enableAggregatingTask = true
}