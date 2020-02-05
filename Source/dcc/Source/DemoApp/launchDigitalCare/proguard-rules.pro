
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*
-keepattributes Signature


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }




#Tagging

-keep public class com.adobe.mobile.** {*;}
-dontwarn com.adobe.mobile.**


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
-keep public class * extends androidx.fragment.app.Fragment {
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

-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void openFileChooser(...);
}

#GooglePLayServices

#-keep class android.support.** {*;}
-keep class android.view.** {*;}

#-keep interface android.support.v13.app.** { *; }
#-keep public class * extends android.support.v13.**
#-keep public class * extends android.app.Fragment

-dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn android.view.**
-dontwarn android.media.session
-dontwarn android.app.**

-dontwarn org.apache.**


-dontwarn com.shamanland.**
-dontwarn uk.co.chrisjenx.**
-dontwarn com.google.android.material


-printmapping out.map
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.

-keepattributes *Annotation*,InnerClasses

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}




# Your library may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface

# This is a configuration file for AppInfra ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.



#GMS
-keep  class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

##General network
-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {*;}
-keepclassmembers public class org.apache.http.** {*;}
-keepattributes InnerClasses,Exceptions

-dontwarn org.apache.**

-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }

##--------------- Jodatime  ----------

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }