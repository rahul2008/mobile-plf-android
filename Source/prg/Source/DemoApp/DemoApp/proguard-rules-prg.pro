# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\310273508\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#
# This ProGuard configuration file illustrates how to process a program
# library, such that it remains usable as a library.
# Usage:
#     java -jar proguard.jar @library.pro
#

# Specify the input jars, output jars, and library jars.
# In this case, the input jar is the program library that we want to process.


# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

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

-dontwarn com.philips.cdp.registration.**
-dontwarn org.apache.**
-dontwarn com.philips.cdp.prxclient.**
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


#GMS
-keep  class com.google.android.gms.* { public *; }


##-keep public class pack.com.progard.** {*;}
##Registration API specific
##General network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions

-keep class org.apache.http.** { *; }



#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }


#appinfra
-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** { *; }
-keep public class com.philips.platform.appinfra.languagepack.model.** { *; }

#uid
-keep public class com.philips.platform.uid.** { *; }
-dontwarn com.philips.platform.uid.**

-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }

#webkit
-keep class android.net.http.SslError
-keep class android.webkit.WebViewClient

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


##Registration API specific
##General and network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,Exceptions
#Hockey app and enabling excpetion catching
-keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}
#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}
-keep public class com.philips.cdp.tagging.** {*;}
#Janrain lib
-keep public class com.janrain.android.** {*;}
-keep class com.janrain.android.Jump$* {*;}
-keep class com.philips.cdp.registration.User$*{*;}
-keep class com.janrain.android.capture.Capture$* {*;}

#Registration API
-keep class com.philips.cdp.registration.** {*;}
-dontwarn com.philips.cdp.registration.**

#HSDP Lib
-keep class com.philips.dhpclient.** {*;}
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class your.class.** {
    public void set*(***);
    public *** get*();
    }

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** { *; }


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
 }

 -keepclassmembers enum * { *; }

 #prxclient model class
 -keep class com.philips.cdp.prxclient.datamodels.** { *; }


