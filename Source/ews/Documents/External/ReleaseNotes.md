EWS for Android - Release Notes
===================================

Version {next}
------------

### Functionality Delivered

### Backwards Compatibility
* N/A

### Features not covered
* N/A

### Breaking Changes
* Removed BufferedWriter out from DiCommLog

### Defects solved
* \#146396 Pressing the continue button on the successful ending of EWS will now send a notification to the launching App. In case of Activity launch of EWS, this notification will be received on onActivityResult and in case of Fragment Launch this would be received via the success method of EwsResultListener

* \#124939 The language files(strings.xml) were improperly named. For example - The naming convention “values-fr-rFR” translates to “French language for france region”, which is not needed. This should simply be “values-fr”. Regional segregation makes sense when we have a different/non-obvious language and region conversions, eg - “values-fr-rCa” - which means “french language for Canadian region”

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
