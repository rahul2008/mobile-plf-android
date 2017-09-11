
-printmapping out.map
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.

-keepattributes *Annotation*,InnerClasses
-keepattributes EnclosingMethod

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

# Your library may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface

# This is a configuration file for AppInfra ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.


-dontwarn com.google.gson.**

-dontwarn com.android.volley.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn okio.**

-dontwarn android.support.**
-dontwarn android.support.v8.**
-dontwarn org.apache.**
-dontwarn com.philips.cdp.prxclient.**
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-keepclassmembers enum * { *; }

#uid
-keep public class com.philips.platform.uid.** { *; }
-dontwarn com.philips.platform.uid.**

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** { *; }

#Product Registration library

-keep class com.philips.cdp.prodreg.model.** {*;}
-keep class com.philips.cdp.prodreg.fragments.** {*;}
-keep class com.philips.cdp.prodreg.register.** {*;}
-keep class com.philips.cdp.prodreg.localcache.** {*;}