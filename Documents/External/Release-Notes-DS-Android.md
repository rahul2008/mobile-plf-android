 **Fixed Defects**
==================

-   DE13349: \[ANDROID\] Application crashed in the below usecase

-   DE13472: \[ANDROID\] Memleak in HomeActivity:
    staticcom.philips.cdp.di.iap.utils. Utility.mProgressDialog

-   DE13831: \[DS/ANDROID\] Memleak in GC ROOT static
    com.philips.platform.core.trackers.DataServicesManager.mDataServicesManager

-   DE13836: \[IAP/ANDROID\] Memleak in GC ROOT
    com.android.volley.CacheDispatcher.

-   DE13928: \[DS/ANDROID\] Memleak in GC ROOT static
    com.philips.platform.core.trackers.DataServicesManager.sDataServicesManager

-   DE14082: Reference app hangs when setting up consent

-   DE14081: DataSync moment values are still displayed eventhough there
    is no user login

**Known Bugs and Workarounds**
==============================

-   DE13962: \[DS/Android\] Temperaturae field is not displayed as
    numeric when 20 digits are entered

-   DE14343: \[Android\] Blank screen observed for 6-8 seconds on data
    sync screen on minimise/maximise app

### **Error Handling**

-   Error messages coming from Data-Core are given to Application as the
    Exception. It’s up to vertical propositions as to how they handle.
    Currently, in the Demo-App, a toast message is shown by extracting
    the error message from Exception.

**Release Description**
=======================

The intention of this release (17.1.1 candidate) is to provide the DataServices to integrate with the PI17.1.1 feature (Measurement Group)+ PI16.5 feature as mentioned below. 
===============================================================================================================================================================================

Release has been done via Artifactory and link for the component is
[here](http://maartens-mini.ddns.htc.nl.philips.com:8081/artifactory/webapp/browserepo.html?3).

Other Libraries and frameworks:

-   PhilipsUIKit (&gt;= 3.1.0)

-   Android Appcompat V7:23.1.1

-   Android Support library V4:23.1.1

-   Android Support Design 23.1.1

-   Philips Registration 8.1.0-SNAPSHOT

-   AppInfra 1.3.0-SNAPSHOT

-   Gson 2.4

-   Ormlite 4.48

-   eventBus 2.4.0

-   jodaTime 2.9.2

-   daggerVer 2.0.2

-   stethoVer 1.3.1

-   retrofitVer 1.9.0

Data Core API version used - 9

**Installation and integration**
================================

Download the Data-Services Library from Artifactory and add to your
project.

Please refer Integration guide for more details
===============================================

**Pre-Requisite**
=================

-   The user should have registered already using User registration
    component to access the Data-Services component.

**Major Features for PI-17.1.1 release**
========================================

### **Moment Synchronization**

A key aspect of data storage within DataCore is the concept of Moments.
Moments describe a set of measurements that are part of the user input
to the system. A Moment is expressed as a JSON format text string.

-   MeasurementGroup is supported

-   Adding a Moment

-   Updating a Moment

-   Deleing a Moment

-   Fetching Moment

-   Saving Moment

-   Syncing Moments between various devices and server

-   Offline support for moment synchronization

-   Data storage for Moments in Local Database

### **Consents**

A user consent, using which data core sends the consented data to HSDP.

-   Adding Consent

-   Deleting Consent

-   Fetching Consent

-   Saving Consent

-   Syncing Consents between various devices and server

-   Offline support for Consents

-   Data Storage for consents in Local Database


