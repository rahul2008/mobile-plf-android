# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ~/Library/Android/sdk/tools/proguard/proguard-android.txt
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

#<========== stuff copied from RAP, because not all components are using consumerProguardFiles ================>
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
#--------------------------------demouapp--------------------------------
-dontwarn com.philips.cdp2.demouapp.fragment.MismatchedPinAppliancesFragment
-dontwarn com.philips.cdp2.demouapp.fragment.port.DevicePortFragment$2

-verbose

#--------------------------------Volley--------------------------------
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.**
-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }

#--------------------------------Gson--------------------------------
-keep class sun.misc.Unsafe { *; }
#Prx
-keep class com.philips.cdp.prxclient.** {*;}
-keep interface com.philips.cdp.prxclient.** { *; }

#--------------------------------Tagging--------------------------------
-keep class com.adobe.mobile.** {*;}
-keep class com.philips.cdp.tagging.** {*;}

#--------------------------------Hockey--------------------------------
-keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}

#--------------------------------Registration--------------------------------
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


-dontwarn com.android.volley.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn okio.**

-dontwarn com.facebook.android.BuildConfig

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

# --------------------------WeChat---------------------------------

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-keep class com.janrainphilips.philipsregistration.wxapi.** {*;}

-keep class com.philips.platform.baseapp.base.wxapi.** {*;}

-keep class com.philips.platform.referenceapp.wxapi.** {*;}

-keep class com.philips.platform.referenceapp.wxapi.WXEntryActivity

-keep class com.tencent.mm.sdk.** {
      *;
  }

#-------------------------Consumer Care Starts -------------------------


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*
-keepattributes Signature


# ----------------------------Gson specific classes --------------------------
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.philips.cdp.prxclient.** {*;}
-keep class com.philips.cdp.prxclient.prxdatamodels.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.google.gson.**


#----------------------------Product Registration library -----------------------
-keep class com.philips.cdp.prodreg.** {*;}
-keep interface com.philips.cdp.prodreg.** {*;}
-keep enum com.philips.cdp.prodreg.** {*;}

# App-framework
-keep public class com.philips.platform.appframework.flowmanager.models.** { *; }


##--------------- ORMLite  ----------

# Keep ORMLite specifics
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keep @com.j256.ormlite.table.DatabaseTable class * { *; }

-dontwarn org.slf4j.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.persistence.**
-dontwarn javax.lang.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn rx.**
-dontwarn android.app.Notification

#-----------------Green Robot Eventbus-----------------------
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class de.greenrobot.event.** { *; }
-keep class de.greenrobot.** { *; }
-keepclassmembers class ** {
    public void onEvent(**);
}
-keepclassmembers,includedescriptorclasses class ** { public void onEvent*(**); }

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

#---------------- Sqlcipher -------------------------
-keep class net.sqlcipher.** {*;}
-keep interface net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** {*;}
-keep interface net.sqlcipher.database.** { *; }
-keep enum net.sqlcipher.**
-keepclassmembers enum net.sqlcipher.** { *; }

#-----------------------------Secure DB--------------------------------
-keep public class com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper.**{ public *;}

#-------------------Dataservices starts here-----------------------------------------
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

#For Dataservices Demo Micro App
-keep class com.philips.platform.dscdemo.pojo.AppCharacteristics { *; }
-keep class com.philips.platform.dscdemo.pojo.AppUserCharacteristics { *; }

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

#------------------Data Services ends here----------------------------------------------------

#HSDP Lib
-keep  class com.philips.dhpclient.** {*;}
-keep  class com.fasterxml.jackson.annotation.** {*;}
-keep  class com.fasterxml.jackson.core.** {*;}
-keep  class com.fasterxml.jackson.databind.** {*;}

#--------------------Tagging--------------------

-keep public class com.adobe.mobile.** {*;}
-keep public class com.philips.cdp.tagging.** {*;}


#--------------------Network--------------------
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#UIKit
-keep class com.shamanland.** {*;}
-keep class uk.co.chrisjenx.** {*;}


#--------------------ConsumerCare--------------------
-keep class com.philips.cdp.digitalcare.** {*;}

-keepclassmembers class *{
  @android.webkit.JavascriptInterface <methods>;
}


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

#------------------------- Consumer Care starts -------------------------

-dontwarn com.philips.cdp.digitalcare.**
#-dontwarn com.philips.cdp.productselection.**
-dontwarn com.adobe.mobile.**
-dontwarn org.apache.**


-dontwarn com.shamanland.**
-dontwarn uk.co.chrisjenx.**


#-------------------------Consumer Care Ends -------------------------

#InAppPurchase
-keep class com.philips.cdp.di.iap.store** {*;}
-keep interface com.philips.cdp.di.iap.store** {*;}
-keep class com.philips.cdp.di.iap.model** {*;}
-keep interface com.philips.cdp.di.iap.model** {*;}
-keep class com.philips.cdp.di.iap.response** {*;}
-keep interface com.philips.cdp.di.iap.response** {*;}
-keep class com.philips.cdp.di.iap.session.** {*;}
-keep interface com.philips.cdp.di.iap.session.** {*;}
-dontwarn com.philips.cdp.di.iap.analytics.**

#Prx
-keep class com.philips.cdp.prxclient.** {*;}
-keep interface com.philips.cdp.prxclient.** { *; }


#--------------------------AppInfra starts here-----------
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions

-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }



#Tagging lib and jar
-keep public class com.adobe.mobile.** {*;}


#app-infra
-keep public class com.philips.platform.appinfra.rest.request.GsonCustomRequest.** { *; }
-keep public class com.philips.platform.appinfra.languagepack.model.** { *; }

#-----------------------------app infra ends here-----------------------------------


#-----------------------------Apeligent starts here-----------------------------------
-dontwarn com.crittercism.**
-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.**{*;}

-keepattributes LineNumberTable
#------------------------------Apeligent ends here------------------------------------

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

# Model classes for test microapp should not be obfuscated
-keep class com.philips.platform.appframework.testmicroappfw.models.** {*;}


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
#<========== stuff copied from RAP, because not all components are using consumerProguardFiles ================>