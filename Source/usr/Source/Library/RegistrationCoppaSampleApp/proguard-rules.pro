# This is a configuration file for ProGuard.
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

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

##-keep public class pack.com.progard.** {*;}

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
-keep public class your.class.** {
    public void set*(***);
    public *** get*();
}


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


######AppInfar

# This is a configuration file for ProGuard.
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

-keepattributes *Annotation*, InnerClasses

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

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
-keep class android.net.http.** { *; }



#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}
-keep public class com.philips.platform.appinfra.tagging.** {*;}
-keep public class com.android.volley.** {*;}

-keep public class com.philips.platform.appinfra.AppInfra { *; }
-keep public class com.philips.platform.appinfra.AppInfra.Builder { *; }
-keepnames public class com.philips.platform.appinfra.AppInfra.Builder

-keepnames public class com.philips.platform.appinfra.AppInfra
-keep public class  com.philips.platform.appinfra.AppInfra$* {
        *;
 }
-keepclassmembers  public class com.philips.platform.appinfra.AppInfra
-keep public class com.philips.platform.appinfra.AppInfraLibraryApplication.**


-keep public class com.philips.platform.appinfra.appidentity.** {
  public protected *;
}
-keep public class com.philips.platform.appinfra.securestorage.** {
  public protected *;
}
-keep public class com.philips.platform.appinfra.logging.** {
   public protected *;
 }
 -keep public class com.philips.platform.appinfra.servicediscovery.** {
    public protected *;
  }




-keep public interface com.philips.platform.appinfra.appidentity.AppIdentityInterface {*;}






-keep class org.apache.commons.logging.**
-keep class com.android.volley.**
-keep interface com.android.volley.**
-dontwarn com.android.volley.**







#notification
-dontwarn android.app.Notification

##End AppInfra

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

