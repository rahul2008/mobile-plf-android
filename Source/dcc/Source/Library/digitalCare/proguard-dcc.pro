#All the packages are obfuscated in DCC.
#For generic rules please refer integration document or demo app proguard rules.


#need for javascript enabled webviews
-keepclassmembers class *{
  @android.webkit.JavascriptInterface <methods>;
}

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.philips.cdp.prxclient.** {*;}
-keep class com.philips.cdp.prxclient.prxdatamodels.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.philips.cdp.digitalcare.contactus.models.** { *; }
-keep class com.philips.cdp.digitalcare.productdetails.model.** { *; }
-keep class com.philips.cdp.digitalcare.prx.subcategorymodel.** { *; }