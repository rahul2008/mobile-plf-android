# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/310188215/Library/Android/sdk/tools/proguard/proguard-android.txt
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

#------------------ For when enums are persistently stored -----------------#
-keep enum com.philips.pins.shinelib.** { *; }
-keepnames enum com.philips.pins.shinelib.**
-keepclassmembers enum com.philips.pins.shinelib.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public protected *;
}

-keepclassmembers class com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake$MealConfiguration {
    private *;
}
#------------------ End: For when enums are persistently stored ------------#

#----------------- For serializable classes --------------------------------#
-keep class com.philips.pins.shinelib.* implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepnames class com.philips.pins.shinelib.* implements java.io.Serializable

-keepclassmembers class com.philips.pins.shinelib.* implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#----------------- End: For serializable classes ---------------------------#