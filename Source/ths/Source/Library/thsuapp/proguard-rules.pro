# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/philips/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-keep class com.philips.platform.ths.faqs.** { *; }

#------------------------------Amwell starts here------------------------------------

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
##

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
##

### AWSDK proguard configuration
-keep class com.americanwell.sdk.internal.** { *; }
###

## keep enums for GSON TypeAdapters$EnumTypeAdapter
-keep public enum com.americanwell.sdk.**{
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
##

## Nucleus
-dontwarn nucleus.view.NucleusActionBarActivity
#

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Vidyo
-keep class com.vidyo.LmiDeviceManager.* { *; }
-dontwarn com.vidyo.LmiDeviceManager.*
##

#------------------------------Amwell ends here------------------------------------