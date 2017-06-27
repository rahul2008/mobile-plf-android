#Android support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v8.renderscript.** { *; }

-verbose

#Gson
-keep class sun.misc.Unsafe { *; }

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

#Webkit (Registration)
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient


#Janrain (Registration)
-keep public class com.janrain.android.** {*;}
-keep  class com.janrain.android.Jump$* {*;}
-keep class com.philips.cdp.registration.User$*{*;}
-keep  class com.janrain.android.capture.Capture$* {*;}

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


-dontwarn com.google.gson.**

-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn okio.**

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

#Ormlite
-dontwarn org.slf4j.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.persistence.**
-dontwarn javax.lang.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*
-keepattributes Signature

#WeChat (Registration)
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.janrainphilips.philipsregistration.wxapi.** {*;}



# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.philips.cdp.prxclient.** {*;}
-keep class com.philips.cdp.prxclient.prxdatamodels.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

##--------------- ORMLite  ----------

# Keep ORMLite specifics
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keep @com.j256.ormlite.table.DatabaseTable class * { *; }


##--------------- Eventbus  ----------

-keepclassmembers class ** {
    !private void onEvent*(**);}

##--------------- Jodatime  ----------

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

##-------------- Retrofit -------------

-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }
-keep class retrofit.** { *; }
-keep interface retrofit.** { *;}

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn rx.**
-dontwarn android.app.Notification

#Data-Services
-keep class com.philips.platform.core.** {*;}
-keep interface com.philips.platform.core.** {*;}

##----------------- Gson -------------------
-keep class com.philips.platform.datasync.moments.UCoreMoment { *; }
-keep class com.philips.platform.datasync.moments.UCoreDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurement { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentsHistory { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentSaveResponse { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroupDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroups { *; }
-keep class com.philips.platform.datasync.consent.UCoreConsentDetail { *; }
-keep class com.philips.platform.datasync.characteristics.UCoreCharacteristics { *; }
-keep class com.philips.platform.datasync.characteristics.UCoreUserCharacteristics { *; }
-keep class com.philips.platform.datasync.settings.UCoreSettings { *; }

#Insight
-keep class com.philips.platform.datasync.insights.UCoreInsight { *; }
-keep class com.philips.platform.datasync.insights.UCoreInsightList { *; }

#Push notification
-keep class com.philips.platform.datasync.PushNotification.UCorePushNotification { *; }

#Device pairing
-keep class com.philips.platform.datasync.devicePairing.UCoreDevicePair { *; }

#Subject Profile
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileRequest { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileResponse { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfileList { *; }
-keep class cdp.philips.com.mydemoapp.pojo.AppUserCharacteristics { *; }
-keep class cdp.philips.com.mydemoapp.pojo.AppCharacteristics { *; }

#HSDP Lib
-keep  class com.philips.dhpclient.** {*;}
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}

#Network
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#UIKit
-keep class com.shamanland.** {*;}
-keep class uk.co.chrisjenx.** {*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.app.Fragment {
    <init>(...);
}
-keep public class * extends android.support.v4.app.Fragment {
    <init>(...);
}

    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class *{
  @android.webkit.JavascriptInterface <methods>;
}


#--------------------------AppInfra starts here-----------
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions
-dontwarn com.philips.platform.appinfra.**

-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

-keep public class com.philips.platform.appinfra.AppInfra { *; }
-keep public class com.philips.platform.appinfra.AppInfra.Builder { *; }
-keepnames public class com.philips.platform.appinfra.AppInfra.Builder
-keep public class com.philips.platform.appinfra.AppInfraSingleton { *; }
-keep public class com.philips.platform.appinfra.GlobalStore { *; }

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
-keep public class com.philips.platform.appinfra.timesync.** {
    public protected *;
  }



-keep public interface com.philips.platform.appinfra.appidentity.AppIdentityInterface {*;}

-keep public interface com.philips.platform.appinfra.AppInfraInterface {*;}

#-----------------------------app infra ends here-----------------------------------