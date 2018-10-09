#Janrain lib
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep  class com.janrain.android.Jump$* {*;}
-keep  class com.janrain.android.capture.Capture$* {*;}

-keepclasseswithmembernames public class com.janrain.android.Jump {*;}
-keepclasseswithmembernames public class com.janrain.android.JumpConfig {*;}
-dontwarn com.janrain.android.**

-keep public class com.philips.cdp.security.SecureStorage {*;}

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontwarn okio.**

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-keep class com.squareup.okhttp3.** {*;}


-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

-dontwarn android.test.**
-dontwarn java.lang.management.**