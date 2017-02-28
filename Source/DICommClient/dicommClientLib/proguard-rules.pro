# For Gson POJOs, the field names are JSON key values and should not be obfuscated
-keepclassmembers class * implements com.philips.cdp2.commlib.core.port.PortProperties {
    <fields>;
}
