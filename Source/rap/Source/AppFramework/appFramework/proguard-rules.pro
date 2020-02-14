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

#-------------------------Registration Starts -------------------------

#Wechat
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.janrainphilips.philipsregistration.wxapi.** {*;}


## New rules for EventBus 3.0.x ##
# http://greenrobot.org/eventbus/documentation/proguard/

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}



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
#This Rule is required for gson
-keep class com.google.gson.** {*;}
-dontwarn com.google.gson.**

-keep class org.xmlpull.v1.** { *; }

#Hockey app and enabling excpetion catching
-keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}


#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}
-keep public class com.philips.cdp.tagging.** {*;}


#HSDP Lib
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }


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

-keepattributes Signature
-keepattributes InnerClasses,EnclosingMethod

#dagger
-dontwarn com.google.errorprone.annotations.**

#-------------------------Registration Ends -------------------------


#----------------------------Product Registration library Start Here -----------------------

#----------------------------Product Registration library End Here -----------------------

#----------------------------Product Registration DemoApp Start Here -----------------------

-keepclassmembers enum * { *; }

#Webkit (Registration)
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#----------------------------Product Registration DemoApp End Here -----------------------


#---------------------------Dataservices starts here-----------------------------------------
#Applied Consumer Proguard Rule
#----------------------------------Data Services ends here-------------------

#-----UIKit
#-keep class com.shamanland.** {*;}
#-keep class uk.co.chrisjenx.** {*;}



#------------------------- Consumer Care starts -------------------------

#need for javascript enabled webviews
-keepclassmembers class *{
  @android.webkit.JavascriptInterface <methods>;
}

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#-dontpreverify
#-dontoptimize

#attributes
-keepattributes *Annotation*
-keepattributes Signature


#-------------------------Consumer Care Ends -------------------------



#------------------------------InAppPurchase starts here------------------------------------
#all below classes are model classes
#------------------------------InAppPurchase ends here------------------------------------

#--------------------------AppInfra starts here-----------
#Applied Consumer Proguard Rule
#-----------------------------app infra ends here-----------------------------------


#--------------------------UappFramework starts here-----------
#Applied Consumer Proguard Rule

#-----------------------------UappFramework ends here-----------------------------------


#-----------------------------PRX starts here-----------------------------------
#Applied Consumer Proguard Rule

#-----------------------------PRX ends here-----------------------------------


#-----------------------------Apeligent starts here-----------------------------------
-dontwarn com.crittercism.**
-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.**{*;}

-keepattributes LineNumberTable
#------------------------------Apeligent ends here------------------------------------

#------------------------------Application specific rules start here------------------------------------
#Detail info at https://www.guardsquare.com/en/proguard/manual/examples#application

-verbose


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
#-keep public class * extends android.support.v4.app.FragmentStackActivity
-keep public class * extends android.app.Fragment {
    <init>(...);
}
-keep public class * extends androidx.fragment.app.Fragment {
    <init>(...);
}

-dontwarn com.google.android.gms.**

# Keep android view

-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
*** get*();
}

# Model classes for test microapp should not be obfuscated
-keep class com.philips.platform.appframework.models.** {*;}
-keep class com.philips.platform.catk.datamodel.CachedConsentStatus { *; }

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

#------------------------------Application specific rules end  here------------------------------------

#------------------------------ Added for fixing Amwell intake 4.0.5 (Should not be checked-in in develop) START ------------------------------------
-ignorewarnings
#------------------------------ Added for fixing Amwell intake 4.0.5 (Should not be checked-in in develop) END ------------------------------------

