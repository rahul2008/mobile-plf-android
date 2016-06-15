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
-dontwarn com.philips.cdp.digitalcare.**
-dontwarn com.philips.cdp.prxclient.**
#Volley
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.**
-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

#Product Registration
#-keep class com.philips.cdp.store** {*;}
#-keep interface com.philips.cdp.store** {*;}
-keep class com.philips.cdp.prodreg** {*;}
-keep class com.philips.cdp.model** {*;}
-keep interface com.philips.cdp.listener** {*;}
-keep class com.philips.cdp.prxrequest** {*;}

