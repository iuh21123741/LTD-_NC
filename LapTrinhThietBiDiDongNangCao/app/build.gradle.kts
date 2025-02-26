plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.laptrinhthietbididongnangcao"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.laptrinhthietbididongnangcao"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //giúp đảm bảo rằng tất cả các thư viện Firebase được sử dụng trong ứng dụng đều có cùng phiên bản tương thích.
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    //Firebase Analytics giúp theo dõi hành vi người dùng trong ứng dụng, như số lượt truy cập, sự kiện (click, đăng nhập, mua hàng,...) và tạo báo cáo trên Firebase Console
    implementation("com.google.firebase:firebase-analytics")
    //Cung cấp các API để xác thực người dùng bằng nhiều phương thức(Email & Password
    //Google Sign-In
    //Facebook, Apple
    //OTP qua số điện thoại)
    implementation("com.google.firebase:firebase-auth")
    //Cung cấp các thành phần UI theo chuẩn Material Design
    implementation ("com.google.android.material:material:1.3.0")
}