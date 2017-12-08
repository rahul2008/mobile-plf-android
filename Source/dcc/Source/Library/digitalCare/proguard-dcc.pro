#All the packages are obfuscated in DCC.
#For generic rules please refer integration document or demo app proguard rules.


#need for javascript enabled webviews
-keepclassmembers class *{
  @android.webkit.JavascriptInterface <methods>;
}
