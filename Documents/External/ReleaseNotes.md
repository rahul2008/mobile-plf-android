BlueLib(/ShineLib) for Android - Release Notes

Version 1.0.0
-------------
### New features:
* [COD-42] Capability for direct bluetooth characteristic manipulations
* [COD-73] Export RSSI in SHNDevice
* [COD-74] Callbacks for service and characteristic discovery

### API changes:
* Added a readRSSI call on SHNDevice
* Added registerDiscoveryListener and unregisterDiscoveryListener on SHNDevice
* Added new capability type: CapabilityBluetoothDirect

### Known issues:
* [BG-234] Google backup might restore previously associated devices


Version 0.7.3
-------------
### New features:
* [BL-295] Add version number to API

### Known issues:
* [BG-234] Google backup might restore previously associated devices


Version 0.7.2
-------------
### Bugs fixed:
* [BG-262] Multiple scanStopped callbacks when scanning is restarted

### Known issues:
* [BG-234] Google backup might restore previously associated devices


Version 0.7.1
-------------
### Bugs fixed:
* [BG-260] Sync problems after re-association

### Known issues:
* [BG-234] Google backup might restore previously associated devices


Version 0.7.0
-------------
### New features:
* [BG-150] Added new SHNDataType: ExtendedWeight 

### Known issues:
* [BG-234] Google backup might restore previously associated devices


Version 0.6.0
-------------
### New features:
* [BG-168] Added javadoc for app developers

### Known issues:
* [BG-234] Google backup might restore previously associated devices

