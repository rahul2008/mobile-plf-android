
#General and network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,Exceptions


#GMS
-keep  class com.google.android.gms.**{ public *;}
-dontwarn com.google.android.gms.**


#gson
-keep interface com.google.gson.** { *; }
-keep  class com.google.gson.** { *; }
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


#prxclient model class
-keep class com.philips.cdp.prxclient.datamodels.** { *; }

##prxclient Request
#-keep class com.philips.cdp.prxclient.RequestManager.**{ public *;}
#-keep class com.philips.cdp.prxclient.request.** { *; }
#
##prxclient Response
#-keep class com.philips.cdp.prxclient.response.** { *; }






