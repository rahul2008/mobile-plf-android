CommLib-all for Android - Release Notes
=======================================

Version {next}
-------------

### Functionality Delivered
* 37438: component now exposes its version and tla via BuildConfig

### Backwards Compatibility
* NA

### Features not covered
* To be filled in at release

### Breaking Changes
See backwards compatibility

### Defects solved
* NA

### Residual anomalies
* To be filled in at release

### Risks and mitigations
* NA

Version 1.3.1
-------------
### Functionality Delivered
* NA

### Backwards Compatibility
* NA

### Features not covered
* NA

### Breaking Changes
* See backwards compatibility

### Defects solved
* NA

### Residual anomalies
* NA

### Risks and mitigations
* NA

Version 1.3.0
-------------

### New features
* 9326 Discovery can now filter on model ids. This is also showcased in the commlib-all test app.

### Bugs fixed

### API Changes
* See new features.

### Known issues
* Notifications not implemented on BLE Strategy.

Version 1.2.2
-------------

### New features
* COM-141 BLE Strategy get/put props implemented.
    - BLE Device cache introduced to contain discovered devices.
* BL-429 Connection is made when doing a put/get.
* COM-247 Connection is disconnected after every request.
* COM-227 Discovery with devices, CPPID is not retreived.
    This wil only work with BLE only nodes.
* CON-113 It is possible to keep a BLE node connected between requests
    by calling enableCommunication on an appliance. You can switch back
    to disconnecting between requests by calling disableCommunication.
* CON-126 Request timeouts are now started when the request is executed, instead of when it's inserted into the request queue.

### Bugs fixed
* DE15085 ProGuard configuration for release builds

### API Changes
* See new features.

### Known issues
* Notifications not implemented on BLE Strategy.
