CommLib-all for Android - Release Notes
=======================================

Version 1.0.0-SNAPSHOT
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

### Bugs fixed
* None.

### API Changes
* See new features.

### Known issues
* Notifications not implemented on BLE Strategy.
