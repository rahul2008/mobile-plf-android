################# PART 1 : default android #######################
	# This is a configuration file for ProGuard.
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

	-keepattributes *Annotation*
	-keep public class com.google.vending.licensing.ILicensingService
	-keep public class com.android.vending.licensing.ILicensingService

	# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
	-keepclasseswithmembernames class * {
	    native <methods>;
	}

	# keep setters in Views so that animations can still work.
	# see http://proguard.sourceforge.net/manual/examples.html#beans
	-keepclassmembers public class * extends android.view.View {
	   void set*(***);
	   *** get*();
	}

	# We want to keep methods in Activity that could be used in the XML attribute onClick
	-keepclassmembers class * extends android.app.Activity {
	   public void *(android.view.View);
	}

	# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
	-keepclassmembers enum * {
	    public static **[] values();
	    public static ** valueOf(java.lang.String);
	}

	-keep class * implements android.os.Parcelable {
	  public static final android.os.Parcelable$Creator *;
	}

	-keepclassmembers class **.R$* {
	    public static <fields>;
	}

	# The support library contains references to newer platform versions.
	# Don't warn about those in case this app is linking against an older
	# platform version.  We know about them, and they are safe.
	-dontwarn android.support.**




################# PART 2 : project specific #######################
	# Add any project specific keep options here:

	-dontwarn com.adobe.mobile.**

	-keepattributes SourceFile,LineNumberTable

	##---------------START: proguard configuration for Gson  ----------
		# Gson uses generic type information stored in a class file when working with fields. Proguard
		# removes such information by default, so configure it to keep all of it.
		-keepattributes Signature

		# Gson specific classes
		-keep class sun.misc.Unsafe { *; }
		-keep class com.google.gson.stream.** { *; }
		-keep class com.google.gson.** { *; }

		# Application classes that will be serialized/deserialized over Gson
		-keep class com.google.gson.examples.android.model.** { *; }
		-keep class com.philips.cl.di.ohc.kids.ble.jsonhelper.** { *; }

	##---------------End: proguard configuration for Gson  ----------



	## Joda Time 2.3

    -dontwarn org.joda.convert.**
    -dontwarn org.joda.time.**
    -keep class org.joda.time.** { *; }
    -keep interface org.joda.time.** { *; }


	##---------------START: proguard configuration for HockeySDK -----------------------------
		-keep public class javax.net.ssl.**
		-keepclassmembers public class javax.net.ssl.** { *;}

		-keep public class org.apache.http.**
		-keepclassmembers public class org.apache.http.** { *;}

		-keepclassmembers class net.hockeyapp.android.UpdateFragment { *; }
	##---------------END: proguard configuration for HockeySDK -----------------------------

	##---------------START: proguard configuration for Janrain library -----------------------------
		-keepclasseswithmembers class * {
		    public <init>(android.content.Context, android.util.AttributeSet);
		}

		-keepclasseswithmembers class * {
		    public <init>(android.content.Context, android.util.AttributeSet, int);
		}


	##---------------END: proguard configuration for Janrain library -----------------------------

	##---------------START: Registration Classes library -----------------------------
    		-keep public class com.philips.cdp.registration.** { *; }
       		-keepclassmembers public class com.philips.cdp.registration.** { *; }
       		-dontwarn com.philips.cdp.registration.**
    ##---------------END: Registrationlibrary -----------------------------

    ##---------------START: Unity Classes library -----------------------------

        -keep class com.philips.cdp.registration.listener.UserRegistrationListener { *; }
        -keepattributes InnerClasses


    ##---------------END: Registrationlibrary -----------------------------

    ##-----------------Registration Lib Start---------------------------
    ##Registration API specific
    ##General network
    -keep public class javax.net.ssl.**
    -keepclassmembers public class javax.net.ssl.** {*;}
    -keepclassmembers public class org.apache.http.** {*;}
    -dontwarn org.apache.**
    -keep class org.apache.http.** { *; }
    -keep class android.net.http.** { *; }

    #Hockey app and enabling excpetion catching
    -keepclassmembers class net.hockeyapp.android.UpdateFragment {*;}
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable

    #Tagging lib and jar
    -keep public class com.adobe.mobile.** {*;}
    -keep public class com.philips.cdp.tagging.** {*;}

    #Janrain lib
    -keep public class com.janrain.android.** {*;}

    #Locale match
    -keep public class com.philips.cdp.localematch.** {*;}

    #Registration API
    -keep public class com.philips.cdp.registration.** {*;}

    #Product Registration API
    -keep public class com.philips.cdp.prodreg.**{*;}

    #HSDP Lib
    -keep  class com.philips.dhpclient.** {*;}
    -keep  class com.fasterxml.jackson.annotation.** {*;}
    -keep  class com.fasterxml.jackson.core.** {*;}
    -keep  class com.fasterxml.jackson.databind.** {*;}


    #GSM
    -keep  class com.google.android.gms.* { public *; }
    -dontwarn com.google.android.gms.**
    -dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

    #webkit
    -keep  class android.net.http.SslError
    -keep  class android.webkit.WebViewClient

    -dontwarn android.webkit.WebView
    -dontwarn android.net.http.SslError
    -dontwarn android.webkit.WebViewClient

    #notification
    -dontwarn android.app.Notification

    -keep class com.android.volley.** { *; }
    -keep interface com.android.volley.** { *; }