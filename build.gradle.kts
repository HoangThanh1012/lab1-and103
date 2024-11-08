// Cấu hình buildscript và plugin cho dự án
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Thêm các plugin cần thiết cho Android
        classpath("com.android.tools.build:gradle:8.5.1") // Cập nhật phiên bản plugin Android Gradle
        classpath("com.google.gms:google-services:4.4.2") // Plugin Google Services
    }
}


