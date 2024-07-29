# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep @androidx.annotation.Keep class * { *; }
-keep class androidx.** { *; }
-keep class android.webkit.** { *; }
-keep class org.chromium.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.bumptech.glide.** { *; }
-keep class com.guicarneirodev.goniometro.** { *; }

# WebView-related classes
-keep class android.webkit.WebView { *; }
-keep class android.webkit.WebSettings { *; }
-keep class org.chromium.** { *; }
-keep class com.android.webview.** { *; }
-keep class com.google.android.webview.** { *; }

# Retain all public and protected methods in classes extending WebView
-keepclassmembers class * extends android.webkit.WebView {
    public *;
    protected *;
}