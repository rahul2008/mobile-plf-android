-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify

-dontwarn com.google.gson.**

-dontwarn com.android.volley.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.**
-dontwarn okio.**

-dontwarn android.support.**
-dontwarn android.support.v8.**
-dontwarn com.philips.cdp.registration.**
-dontwarn org.apache.**
-dontwarn com.philips.cdp.prxclient.**

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

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

#UIKIT
-keep class com.philips.cdp.uikit.** {*;}

#Product Registration library
-keep class com.philips.cdp.prodreg.** {*;}
-keep interface com.philips.cdp.prodreg.** {*;}
-keep enum com.philips.cdp.prodreg.** {*;}

#Android support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v8.renderscript.** { *; }

#Volley
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.**
-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.**{*;}
#-keep class com.google.gson.stream.** { *; }

#Prx
-keep class com.philips.cdp.prxclient.** { *; }
-keep interface com.philips.cdp.prxclient.** { *; }
-keep enum com.philips.cdp.prxclient.** { *; }


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
-keep public class com.philips.cdp.security.SecureStorage {
public static void init(android.content.Context);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static java.lang.String objectToString(java.io.Serializable);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static java.lang.Object stringToObject(java.lang.String);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static void migrateUserData(java.lang.String);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static byte[] encrypt(java.lang.String);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static byte[] decrypt(byte[]);
}
-keep public class com.philips.cdp.security.SecureStorage {
public static void generateSecretKey();
}
-keepclasseswithmembernames public class com.janrain.android.** {*;}
-keepclasseswithmembernames public class com.janrain.android.Jump {*;}
-keepclasseswithmembernames public class com.janrain.android.JumpConfig{*;}
-keepclasseswithmembernames public class com.janrain.android.TradSignInUi{*;}
#Locale match
-keep public class com.philips.cdp.localematch.** {*;}
-keep interface com.philips.cdp.localematch.** {*;}
#Registration API
-keep class com.philips.cdp.registration.** {*;}
-dontwarn com.philips.cdp.registration.**
#HSDP Lib
-keep class com.philips.dhpclient.** {*;}
-keep class com.fasterxml.jackson.annotation.** {*;}
-keep class com.fasterxml.jackson.core.** {*;}
-keep class com.fasterxml.jackson.databind.** {*;}
#GSM
-keep class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
#webkit
-keep class android.net.http.SslError
-keep class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
#notification
-dontwarn android.app.Notification
