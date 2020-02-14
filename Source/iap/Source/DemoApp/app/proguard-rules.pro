# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify

-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontwarn com.google.gson.**

-dontwarn com.android.volley.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn okio.**

-dontwarn com.philips.cdp.registration.**
-dontwarn org.apache.**
-dontwarn com.philips.cdp.digitalcare.**
-dontwarn com.philips.cdp.prxclient.**

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
 *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

#Enumeration
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

#Static
-keepclassmembers class **.R$* {
 public static <fields>;
}

#Android support library
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.** { *; }
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }
#-keep class android.support.v8.renderscript.** { *; }

#Volley
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.**
-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }
-keep class com.fasterxml.jackson.annotation.**{*;}

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }



#Prx
-keep class com.philips.cdp.prxclient.** { *; }
-keep interface com.philips.cdp.prxclient.** { *; }
-keep enum com.philips.cdp.prxclient.** { *; }

#LocaleMatch
-keep class com.philips.cdp.localematch.** {*;}
-keep interface com.philips.cdp.localematch.** {*;}

#Tagging
-keep class com.adobe.mobile.** {*;}
-keep class com.philips.cdp.tagging.** {*;}

#Hockey
-keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}

#Registration
-keep class com.philips.cdp.registration.** {*;}

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#GMS (Registration)
-keep  class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

#Webkit (Registration)
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

#notification (Registration)
-dontwarn android.app.Notification

#Janrain (Registration)
-keep public class com.janrain.android.** {*;}
-keep  class com.janrain.android.Jump$* {*;}
-keep class com.philips.cdp.registration.User$*{*;}
-keep  class com.janrain.android.capture.Capture$* {*;}



#HSDP Lib (Registration)
-keep  class com.philips.dhpclient.** {*;}


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

    #InAppPurchase
    -keep class com.philips.cdp.di.iap.store.AbstractStore {*;}
    -keep class com.philips.cdp.di.iap.store.HybrisStore {*;}
    -keep class com.philips.cdp.di.iap.store.IAPUser {*;}
    -keep class com.philips.cdp.di.iap.store.LocalStore {*;}
    -keep class com.philips.cdp.di.iap.store.StoreConfiguration {*;}
    -keep class com.philips.cdp.di.iap.store.StoreController {*;}
    -keep interface com.philips.cdp.di.iap.store.StoreListener{*;}

    -keep class com.philips.cdp.di.iap.model** {*;}
    -keep interface com.philips.cdp.di.iap.model** {*;}

    -keep class com.philips.cdp.di.iap.response** {*;}
    -keep interface com.philips.cdp.di.iap.response** {*;}