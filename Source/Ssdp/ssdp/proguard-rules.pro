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
-keep class com.philips.cl.di.common.ssdp.lib.SsdpService {
    <fields>;
    protected void ssdpCallback(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String);
    private native int openSocket();
    private native void closeSocket();
    private native int registerListener();
    private native int sendBroadcastMX3();
    private native int sendBroadcastMX5();
    private native int startDiscovery();
    private native void stopDiscovery();
}
