
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


#UIKit
-keep public class com.philips.cdp.uikit.customviews.**{*;}



#LocaleMatch

-keep public class com.philips.cdp.localematch.** {*;}


#Network

-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }

#Calligraphy Library
-keep class uk.co.chrisjenx.calligraphy.** {*;}

#PRX Component
-keep class com.philips.cdp.prxclient.**{*;}


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

    -dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
    -dontwarn  com.philips.cdp.productselection.R$id

  



#GooglePLayServices


-keep class android.support.v13.** { *; }  
-keep interface android.support.v13.app.** { *; }
-keep class com.google.android.gms.** { *; }
-keep android.support.v4.app.**{*;}
-keep public class * extends android.support.v13.**   
-keep public class * extends android.app.Fragment

-dontwarn com.google.android.gms.maps.**
-dontwarn com.google.android.gms.**
-dontwarn com.philips.cdp.uikit.**
-dontwarn com.philips.cdp.digitalcare.**
-dontwarn com.philips.cdp.productselection.**
-dontwarn android.support.v13.**
-dontwarn uk.co.chrisjenx.calligraphy.**
-dontwarn org.apache.**
-dontwarn com.philips.cdp.prxclient.**
-dontwarn com.philips.cdp.localematch.**
-dontwarn android.support.v4.app.**


