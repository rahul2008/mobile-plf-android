#Janrain lib
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

#-keep public class com.janrain.android.** {*;}
-keep  class com.janrain.android.Jump$* {*;}
-keep  class com.janrain.android.capture.Capture$* {*;}

#-keepclasseswithmembernames public class com.janrain.android.** {*;}
-keepclasseswithmembernames public class com.janrain.android.Jump {*;}
-keepclasseswithmembernames public class com.janrain.android.JumpConfig {*;}
-dontwarn com.janrain.android.**

-keep public class com.philips.cdp.security.SecureStorage {*;}

#WeChat
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}