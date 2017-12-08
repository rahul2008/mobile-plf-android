BlueLib(/ShineLib) for Android - Release Notes
=======================================


Version {next}
-------------

### Functionality Delivered

### Backwards Compatibility

### Features not covered

### Breaking Changes
* The `ScanRecord` function `getManufacturerSpecificData()` is replaced by `getManufacturerSpecificData(int manufacturerId)`. Previously the first two bytes of the manufacturer where used to check the manufacturer, now this is done internally and the data of a specific manufacturer can be requested. If the data is not found `null` is returned.

### Defects solved
* \#33811 Battery service now has working notifications.
* \#85159 DicommStreamingService is now reaching 'Ready' state on Nexus 6P and Samsung S4
* \#88153 Extended bond connection timeout for slow phones
* \#88152 Added a timeout on the disconnect procedure
* \#88149 If no services are found during discovery, retry the discovery
* \#88154 Fix the flow of bonding with timers
* \#88146 Refresh the device cache on Android 7.0, this prevents a corrupt BLE cache
* \#87558 Devices are no longer listed multiple times in device discovery
* \#89116 Add support for unencrypted moonshine streaming service
* \#89113 Bug resolved on retry firmware update after interrupted connection

### Residual anomalies

### Risks and mitigations



Version 3.1.0
-------------

### Functionality Delivered
* Component now exposes its version and tla via BuildConfig

### Backwards Compatibility
* NA

### Features not covered
* NA

### Defects solved
* \#56444 Fix firmware upgrade maxchunksize.
* \#52917 Fixed ConcurrentModificationException in logger

### Residual anomalies
* Bug 18932: Android] After Unpairing Moonshine and Pairing it back to same User is failing.
* Bug 18942:[Android] Engineering Issue: DiCommChannel is not resilient against leading garbage
* Bug 18963:[Android] Thermometer stays in searching status itself if turn off bluetooth while pairing and even if turn ON bluetooth pairing never continues
* Bug 18940:[Android] Engineering Issue: NPE when calling readRssi()
* Bug 18941:[Android] Engineering Issue: NullPointerException in BlueLib ExampleApp
* Bug 14440:Bug 49686:[Android] Exception logged - "exceeded expected execution time of 50 ms"

### Risks and mitigations
* NA



Version 2.3.5
-------------

### Functionality Delivered
* NA

### Backwards Compatibility
* NA

### Features not covered
* To be filled in at release

### Breaking Changes
See backwards compatibility

### Defects solved
* [35185] Filting on UUID will now be done by the Android OS which reduce the amount of wrong results from discovery on certain phones. (For example on the Nexus 6P)

### Residual anomalies
* To be filled in at release

### Risks and mitigations
* NA

Version 2.3.4
-------------
No changes.

Version 2.3.3
-------------
No changes.

Version 2.3.2
-------------
### New features
* [COM-141] Exposed DiComm package for reuse
* Added new capability type: DI_COMM and corresponding capability definition: CapabilityDiComm
* [BL-147] Custom capabilities added. It is now possible to implement capabilities without modifying BlueLib.
* [CON-96] Discovery of DiComm devices

### Bugs fixed
* [DE15085] ProGuard configuration for release builds

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 2.0.3
-------------
### New features
* The dafault implementation of SHNDevice interface does not ignore connect calls:
	- In state Connected onStateUpdated callback will be issued.
	- In state Connecting no callback.
	- In state Disconnecting onFailedToConnect callback will be issued.
	- In state Disconnected onStateUpdated callback will be issued.

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 2.0.2
-------------
### New features
* Added data types: IR light, Lux value (For Moonlight/Circadian)

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 2.0.1
-------------
### New features
* [CON-31] Made tests compatible with the release build variant. Added new exception type: TimeoutException. TimeoutException is generated in case the injected persistent storage takes too long to perform its operations.

### Bugs fixed
* [BL-385] NPE when disconnecting BtGatt

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 2.0.0
-------------
### Bugs fixed
* [BL-241] Implemented retry mechanism for perhiral connection

### API changes
* Deprecated SHNDeviceWrapper connect call with parameters
* Added a connect call on SHNDevice that allows to provide a timeout for establishing BLE connection

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 1.0.0
-------------
### New features
* [COD-42] Capability for direct bluetooth characteristic manipulations
* [COD-73] Export RSSI in SHNDevice
* [COD-74] Callbacks for service and characteristic discovery

### API changes
* Added a readRSSI call on SHNDevice
* Added registerDiscoveryListener and unregisterDiscoveryListener on SHNDevice
* Added new capability type: CapabilityBluetoothDirect

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 0.7.3
-------------
### New features
* [BL-295] Add version number to API

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 0.7.2
-------------
### Bugs fixed
* [BG-262] Multiple scanStopped callbacks when scanning is restarted

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 0.7.1
-------------
### Bugs fixed
* [BG-260] Sync problems after re-association

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 0.7.0
-------------
### New features
* [BG-150] Added new SHNDataType: ExtendedWeight

### Known issues
* [BL-234] Google backup might restore previously associated devices


Version 0.6.0
-------------
### New features
* [BG-168] Added javadoc for app developers

### Known issues
* [BL-234] Google backup might restore previously associated devices
