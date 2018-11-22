EWS for Android - Release Notes
===================================

Version {next}
------------

### Functionality Delivered
* \#128922 EWS Flow Improvements - Moved Location Permission in start of EWS flow from later stage, Added new functionality for listing available networks from appliance to solve 2.4 vs 5 GHz network issue, Added new Screen for Network Not Listed Troubleshooting
* \#144406 Added new public setter API 'setEwsResultListener' in 'BaseContentConfiguration' to let applications set EwsResultListener
* \#144406 Added new callback API 'onEWSCancelled' in 'EwsResultListener' to let applications intimate that EWS has been cancelled either from 'Yes, cancel setup' button of popup displayed or on back press of Get Started/First Screen
* \#159975 Added new public variable 'deviceDiscoveryTimeoutInterval' in 'BaseContentConfiguration' to let applications configure timeout of device discovery after Wi-Fi credentials are written to the appliance.

### Backwards Compatibility
* N/A

### Features not covered
* N/A

### Breaking Changes
* Removed BufferedWriter out from DiCommLog
* \#144406 Added new callback API 'onEWSCancelled' in 'EwsResultListener' interface, which applications need to implement

### Defects solved
* \#146396 Pressing the continue button on the successful ending of EWS will now send a notification to the launching App. In case of Activity launch of EWS, this notification will be received on onActivityResult and in case of Fragment Launch this would be received via the success method of EwsResultListener

* \#124939 The language files(strings.xml) were improperly named. For example - The naming convention “values-fr-rFR” translates to “French language for france region”, which is not needed. This should simply be “values-fr”. Regional segregation makes sense when we have a different/non-obvious language and region conversions, eg - “values-fr-rCa” - which means “french language for Canadian region”

* \#157564 According to logs provided, it gives inconsistent crash "invoke virtual method 'boolean java.lang.String.equals(java.lang.Object)' on a null object reference" on line return ssidProvider.getCurrentSsid() == null || ssidProvider.getCurrentSsid().equals(networkNode.getNetworkSsid()); in LanCommunicationStrategy.java which means sometimes first condition is not null and it executes second condition and that time ssidProvider.getCurrentSsid() may come null, so changed code to String currentSsid = ssidProvider.getCurrentSsid(); return currentSsid == null || currentSsid.equals(networkNode.getNetworkSsid());

### Residual anomalies
* N/A

### Risks and mitigations
* N/A

Version 1801
------------

### Functionality Delivered
* N/A

### Backwards Compatibility
* N/A

### Features not covered
* N/A

### Breaking Changes
* N/A

### Defects solved
* \#124941 Made EWS DLS compliant (by changing to UID dependency, instead of deprecated UIT)

### Residual anomalies
* N/A

### Risks and mitigations
* N/A

Version 2017.5.0
----------------

### Functionality Delivered
First delivery of EWS

### Backwards Compatibility
* N/A

### Features not covered
* N/A

### Breaking Changes
* N/A

### Defects solved
* N/A

### Residual anomalies
* N/A

### Risks and mitigations
* N/A
