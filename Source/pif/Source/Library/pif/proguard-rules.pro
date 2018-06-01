-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

##--------------- Jodatime  ----------

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
-keep com.philips.platform.pif.chi.datamodel.ConsentStatus { *; }
-keep com.philips.platform.pif.chi.datamodel.ConsentStates { *; }