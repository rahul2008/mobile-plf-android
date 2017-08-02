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

# ICP Proguard rules ---- START -----
-injars libs

-keep class com.philips.icpinterface.ComponentDetails {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeGetComponentDetails();
}

-keep class com.philips.icpinterface.DataCollection {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeUploadData();
	private native int nativeGetUploadedSize();
}

-keep class com.philips.icpinterface.DataCollection$* {
	<fields>;
}

-keep class com.philips.icpinterface.DateTimeInfo {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeGetDateTime();
}

-keep class com.philips.icpinterface.DeProvision {
	<fields>;
	private boolean deleteFile(java.lang.String);
	private void  callbackFunction(int,int);
	private native int nativeDeProvision();
}

-keep class com.philips.icpinterface.DownloadData {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeClientDownloadData();
}

-keep class com.philips.icpinterface.Event {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeGetEvents();
}

-keep class com.philips.icpinterface.EventPublisher {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativePublishEvent();
	private native int nativeCancelEvent();
	private native int nativeGetEventDistributionList();
}

-keep class com.philips.icpinterface.EventSubscription {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeStartSubscription();
	private native int nativeStopSubscription();
}

-keep class com.philips.icpinterface.FileDownload {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeDownloadFile();
	private native int nativeAbortDownload();
	private native int nativeGetNrOfBytesDownloaded();
}

-keep class com.philips.icpinterface.SSLConnectivity {
    private javax.net.ssl.SSLSocketFactory initializeSession();
    private boolean loadCertificates(com.philips.icpinterface.ICPClientToAppInterface);
	private void checkNetworkSate(com.philips.icpinterface.ICPClientToAppInterface);
	private boolean unloadCertificates();
	private void setCertificateExpiry(int);
	private boolean loadProxyDetails( );
	private java.lang.String proxyIP();
	private int proxyPort();
}

-keep class com.philips.icpinterface.ICPClientToAppInterface {
}

-keep class com.philips.icpinterface.PairingService {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeAddRelationshipRequest();
	private native int nativeGetRelationshipsRequest();
	private native int nativeAddPermissionsRequest();
	private native int nativeRemovePermissionsRequest();
	private native int nativeGetPermissionsRequest();
	private native int nativeQueryPermissionRequest();
	private native int nativeRemoveRelationshipRequest();
	private native int nativeResetTTLRequest();
	private native int nativeSetMyMetadataRequest();
	private native int nativeGetMyMetadataRequest();
	private native int nativeSetRelationshipMetadataRequest();
}

-keep class com.philips.icpinterface.PeripheralProvision {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativePeripheralProvision();
}

-keep class com.philips.icpinterface.Provision {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeProvision();
}

-keep class com.philips.icpinterface.Registration {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeAcceptTermsAndConditions();
	private native int nativeQueryTCStatus();
	private native int nativeRegisterProduct();
	private native int nativeQueryRegistrationStatus();
	private native int nativeUnRegisterProduct();
}

-keep class com.philips.icpinterface.ResetDevice {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeResetDevice();
}

-keep class com.philips.icpinterface.ServicePortal {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeGetServicePortals();
}

-keep class com.philips.icpinterface.ServicePortal$* {
	<fields>;
}

-keep class com.philips.icpinterface.SignOn {
	<fields>;
	private void  callbackFunction(int,int);
	private byte[] readCacheFileData(java.lang.String);
	private java.lang.String getValueForKey(java.lang.String);
	private byte[] getUpdatedByteArray(java.lang.String, java.lang.String);
	private boolean createCacheFile(byte[] );
	private native int nativeSignOn();
	private native int nativeInit();
	private native int nativeClose();
	private native void nativeSetLocale();
	private native int nativeComponentUpdate();
	private native int nativeDevicePropertiesUpdate();
	private native java.lang.String nativeClientVersion();
	private native int nativeGetErrorDetails();
	private static native boolean nativeIsServiceEnabled(int);
	private static native boolean nativeIsTLSEnabled();
}


-keep class com.philips.icpinterface.ThirdPartyNotification {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeRegisterProtocolAddress();
	private native int nativeUnRegisterProtocolAddress( );
}

-keep class com.philips.icpinterface.TimeZones {
	<fields>;
	private void  callbackFunction(int,int);
	private native int nativeGetTimeZones();
}

-keep class com.philips.icpinterface.TimeZones$*{
	<fields>;
}

-keep class com.philips.icpinterface.data.ComponentInfo {
	<fields>;
}
-keep class com.philips.icpinterface.data.DateTime {
	<fields>;
}
-keep class com.philips.icpinterface.data.DeviceProperty {
	<fields>;
}
-keep class com.philips.icpinterface.data.EventLocal {
	<fields>;
}
-keep class com.philips.icpinterface.data.GCMAProtocolDetails {
	<fields>;
}
-keep class com.philips.icpinterface.data.NVMComponentInfo {
	<fields>;
}
-keep class com.philips.icpinterface.data.NVMDeviceProperty {
	<fields>;
}
-keep class com.philips.icpinterface.data.PairingEntitiyReference {
	<fields>;
}
-keep class com.philips.icpinterface.data.PairingInfo {
	<fields>;
}
-keep class com.philips.icpinterface.data.PairingReceivedRelationships$PairingEntity {
	<fields>;
}
-keep class com.philips.icpinterface.data.PairingReceivedRelationships {
	<fields>;
}
-keep class com.philips.icpinterface.data.PairingRelationship {
	<fields>;
}
-keep class com.philips.icpinterface.data.PeripheralDevice {
	<fields>;
}

-keep class com.philips.icpinterface.data.IdentityInformation {
	<fields>;
}

-keep class com.philips.icpinterface.data.PeripheralBootstrapDetails {
	<fields>;
}

-keep class com.philips.icpinterface.data.ErrorDetails {
	<fields>;
}

-keepclasseswithmembernames class * {
    native <methods>;

}

# ICP Proguard rules ---- END -----
