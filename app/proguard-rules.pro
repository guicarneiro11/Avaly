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
# Manter nomes de classes, métodos e campos para bibliotecas específicas
-keep class com.google.** { *; }
-keep class okhttp3.** { *; }

# Manter nomes de classes anotadas com @Keep
-keep @interface androidx.annotation.Keep
-keep class * {
    @androidx.annotation.Keep *;
}

# Manter classes utilizadas para reflection
-keepclassmembers class * {
    *;
}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Remover logging e debug
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int w(...);
    public static int v(...);
    public static int i(...);
    public static int e(...);
}