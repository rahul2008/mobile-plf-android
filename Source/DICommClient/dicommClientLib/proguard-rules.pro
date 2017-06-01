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

# Gson rules
-keepattributes Signature, *Annotation*
-keep class sun.misc.Unsafe { *; }

# For Gson POJOs, the field names are JSON key values and should not be obfuscated
-keepclassmembers class * implements com.philips.cdp2.commlib.core.port.PortProperties {
    <fields>;
}

-keep class com.philips.cl.di.common.ssdp.lib.SsdpService {
    protected void ssdpCallback(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String);
    native <methods>;
}

# Joda time does not include this by itself
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
