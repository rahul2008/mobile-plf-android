
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.philips.cdp.prxclient.prxdatamodels.** { *; }



#Tagging 
 
-keep public class com.adobe.mobile.** {*;}
-keep public class com.philips.cdp.tagging.** {*;}


#LocaleMatch

-keep public class com.philips.cdp.localematch.** {*;}


#Network
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }


#ConsumerCare


-keep public class * extends android.app.Activity 
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver


-keepclasseswithmembers class * 
  {
    public <init>(android.content.Context, android.util.AttributeSet);
  }

-keepclasseswithmembers class * 
  {
    public <init>(android.content.Context, android.util.AttributeSet, int);
  }

   -keep class * extends android.view.View 
    { 
                 
                  void set*(***);
                   *** get*();
    }

   -keepclassmembers class * 
    {
                    static final %                *;
                    static final java.lang.String *;
    }
  
-dontwarn com.philips.cdp.digitalcare.**
-dontwarn com.philips.cdp.productselection.**


#GooglePLayServices

-dontwarn android.support.v13.**    
-keep class android.support.v13.** { *; }  
-keep interface android.support.v13.app.** { *; }  
-keep public class * extends android.support.v13.**   
-keep public class * extends android.app.Fragment

-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.** { *; }


