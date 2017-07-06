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



#GSM
-keep  class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

#webkit
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

#notification
-dontwarn android.app.Notification
-dontwarn okio.**
-keep class com.squareup.** { *; }
-keep class java.nio.**
-keep class org.codehaus.**

-dontwarn com.janrain.android.**
-dontwarn java.nio.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

##-keep public class pack.com.progard.** {*;}
##Registration API specific
##General network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions
-dontwarn com.philips.platform.appinfra.**

-dontwarn org.apache.**
-keep class org.apache.http.** { *; }



#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }

-keep public class com.philips.platform.appinfra.**{*;}
-keep interface com.philips.platform.appinfra.**{*;}

##appinfra
#-keep public class com.philips.platform.appinfra.AppInfra.** {*;}
#-keep public interface com.philips.platform.appinfra.AppInfraInterface.** {*;}
#-keep public interface com.philips.platform.appinfra.logging.LoggingInterface.** {*;}
#-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** {*;}
#-keep public interface com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
