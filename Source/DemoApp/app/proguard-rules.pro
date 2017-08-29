# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\310240027\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

-verbose

#--------------------------------Tagging--------------------------------
-keep class com.adobe.mobile.** {*;}
-keep class com.philips.cdp.tagging.** {*;}

#--------------------------------Registration--------------------------------
-keep class com.philips.cdp.registration.** {*;}

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#GMS (Registration)
-keep  class com.google.android.gms.* { public *; }
-keep class com.google.android.gms.** { *; }

#Webkit (Registration)
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient


#Janrain (Registration)
-keep public class com.janrain.android.** {*;}
-keep  class com.janrain.android.Jump$* {*;}
-keep class com.philips.cdp.registration.User$*{*;}
-keep  class com.janrain.android.capture.Capture$* {*;}


-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**

-dontwarn android.support.**
-dontwarn android.support.v8.**
-dontwarn com.philips.cdp.registration.**
-dontwarn com.philips.cdp.platform.**
-dontwarn org.apache.**
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

#notification (Registration)
-dontwarn android.app.Notification

-keepclasseswithmembernames class * {
    native <methods>;
}
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

#-------------------------Consumer Care Starts -------------------------


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn javax.annotation.**

-dontwarn okio.**
-dontwarn rx.**
-dontwarn android.app.Notification

#HSDP Lib
-keep  class com.philips.dhpclient.** {*;}
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}


#--------------------GooglePLayServices--------------------

-keep class android.support.** {*;}
-keep class android.view.** {*;}

-keep interface android.support.v13.app.** { *; }
-keep public class * extends android.support.v13.**
-keep public class * extends android.app.Fragment
-keep class com.philips.cdp.uikit.customviews.**
-keep class com.philips.cdp.productselection.**
-keep class com.philips.cdp.productselection.utils.ProductSelectionLogger.**
-keep class com.philips.cdp.productselection.ProductModelSelectionHelper.**

-dontwarn com.google.android.gms.**

-dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn android.view.**
-dontwarn android.media.session
-dontwarn android.app.**

#--------------------------AppInfra starts here-----------
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions

-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }


#app-infra
-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** { *; }
-keep public class com.philips.platform.appinfra.languagepack.model.** { *; }

#-----------------------------app infra ends here-----------------------------------


#------------------------------Application specific rules start here------------------------------------
#Detail info at https://www.guardsquare.com/en/proguard/manual/examples#application

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
      public <init>(java.util.Map);
 }

# Keep fragments
-keep public class * extends android.support.v4.app.FragmentStackActivity
-keep public class * extends android.app.Fragment {
    <init>(...);
}
-keep public class * extends android.support.v4.app.Fragment {
    <init>(...);
}

# Keep android view

-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
*** get*();
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

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.* { *;}
-dontwarn okio.