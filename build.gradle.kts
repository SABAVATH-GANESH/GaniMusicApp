//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    id("com.google.gms.google-services") version "4.4.1" apply false // ✅ Add this line
//}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// ❌ DO NOT use 'allprojects { repositories { ... } }'.
// ✅ Repositories are now declared in settings.gradle.kts only.

