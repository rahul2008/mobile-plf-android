#General
-verbose

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

-keepclasseswithmembernames class * {
    native <methods>;
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
-dontwarn android.support.**
-dontwarn android.support.v8.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v8.renderscript.** { *; }

#Hockey
-keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}

#User Registration starts here
#Registration
-dontwarn com.philips.cdp.registration.**
-dontwarn com.philips.cdp.platform.**
-dontwarn org.apache.**

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn android.app.Notification
-dontwarn okio.**

-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*
-keepattributes Signature

-keep class com.philips.cdp.registration.** {*;}

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

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

#Network
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#GMS (Registration)
-keep  class com.google.android.gms.* { public *; }

#WeChat (Registration)
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.janrainphilips.philipsregistration.wxapi.** {*;}

#Webkit (Registration)
-keep  class android.net.http.SslError
-keep  class android.webkit.WebViewClient

#HSDP Lib
-keep  class com.philips.dhpclient.** {*;}
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}

#Janrain (Registration)
-keep public class com.janrain.android.** {*;}
-keep  class com.janrain.android.Jump$* {*;}
-keep class com.philips.cdp.registration.User$*{*;}
-keep  class com.janrain.android.capture.Capture$* {*;}

#User Registration ends here

#Prx client
-keep class com.philips.cdp.prxclient.** {*;}
-keep class com.philips.cdp.prxclient.prxdatamodels.** { *; }

#UIKit starts here
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
#UIKit ends here

#App Infra Starts here
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

#App Infra Ends here

#Dataservices starts here
#Pojo classes required by Retorfit to reflect the response

#Data-Services Moments
-keep class com.philips.platform.datasync.moments.UCoreMoment { *; }
-keep class com.philips.platform.datasync.moments.UCoreDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurement { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentsHistory { *; }
-keep class com.philips.platform.datasync.moments.UCoreMomentSaveResponse { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroupDetail { *; }
-keep class com.philips.platform.datasync.moments.UCoreMeasurementGroups { *; }

#Data-Services Consent
-keep class com.philips.platform.datasync.consent.UCoreConsentDetail { *; }

#Data-Services Characteristics
-keep class com.philips.platform.datasync.characteristics.UCoreCharacteristics { *; }
-keep class com.philips.platform.datasync.characteristics.UCoreUserCharacteristics { *; }

#Data-Services Settings
-keep class com.philips.platform.datasync.settings.UCoreSettings { *; }

#Data-Services Insight
-keep class com.philips.platform.datasync.insights.UCoreInsight { *; }
-keep class com.philips.platform.datasync.insights.UCoreInsightList { *; }

#Data-Services Push notification
-keep class com.philips.platform.datasync.PushNotification.UCorePushNotification { *; }

#Data-Services Device pairing
-keep class com.philips.platform.datasync.devicePairing.UCoreDevicePair { *; }

#Data-Services Subject Profile
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileRequest { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileResponse { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile { *; }
-keep class com.philips.platform.datasync.subjectProfile.UCoreSubjectProfileList { *; }

#Data Services ends here

#DS Demo micro app
-keep class com.philips.platform.dscdemo.pojo.AppCharacteristics { *; }
-keep class com.philips.platform.dscdemo.pojo.AppUserCharacteristics { *; }

#Green Robot
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class de.greenrobot.event.** { *; }
-keep class de.greenrobot.** { *; }
-keepclassmembers class ** {
    public void onEvent(**);
}
-keepclassmembers,includedescriptorclasses class ** { public void onEvent*(**); }

#Jodatime
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

#Retrofit
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn rx.**
-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }
-keep class retrofit.** { *; }
-keep interface retrofit.** { *;}

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#Gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.gson.examples.android.model.** { *; }

#Ormlite
-dontwarn org.slf4j.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.persistence.**
-dontwarn javax.lang.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**

-keep public class com.j256.ormlite.** {*;}
-keep class com.j256.ormlite.** { *; }
-keep interface com.j256.ormlite.** { *; }
-dontwarn com.j256.ormlite.**
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep @com.j256.ormlite.table.DatabaseTable class * { *; }

#sqlcipher
-keep class net.sqlcipher.** {*;}
-keep interface net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** {*;}
-keep interface net.sqlcipher.database.** { *; }
-keep enum net.sqlcipher.**
-keepclassmembers enum net.sqlcipher.** { *; }

#Secure DB
-keep public class com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper.**{ public *;}

# from dagger.android.DispatchingAndroidInjector
-dontwarn com.google.errorprone.annotations.*