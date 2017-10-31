CommLib for Android - Release Notes
=======================================

Version {next}
-------------

### Functionality Delivered
* CommLib - \#65109 Removed connection state machine from DiscoveryManager.
	* Including the `LanDiscoveryStrategy`, `CombinedCommunicationStrategy` and `CloudCommunicationStrategy`.
* CommLib - \#65428 Persistent NetworkNode Storage.
	* Store an Appliance by calling `ApplianceManager.storeAppliance(appliance)`
	* Stop persisting an Appliance by calling `ApplianceManager.forgetStoredAppliance(appliance)`
	* Persistent storage will only store information in `NetworkNode` so if you need to store additional data or state from your own Appliance implementation you can implement `ApplianceDatabase` and give it to `new CommCentral(applianceFactory, applianceDatabase, transportContexts...)`
* CommLib - \#85443 SSDP is rewritten from C and JNI in pure Java.
* CommLib - CommCentral now throws a `UnsupportedOperationException` whenever a second instance is created.

### Backwards Compatibility
* CommLib - `Appliance.enableCommunication` is no longer needed before a subscription.
* CommLib - Running the pairing process (through `PairingHandler`) no longer automatically stores the appliance to the database upon successful completion. Use `ApplianceManager.storeAppliance()` method manually as alternative.

### Features not covered
* To be filled in at release

### Breaking Changes
* CommLib - `NetworkNode` and `Appliance` no longer have a `(get/set)ConnectionState`. In return `Appliance`, `CommunicationStrategy` and `TransportContext` now have an `isAvailable()` method to inform about the availablity of a connection.
* CommLib - `enableCommunication` for any `CommunicationStrategy` doesn't have parameters anymore. `SubscriptionEventListener` can be added with the `addSubscriptionEventListener` call.
* CommLib - `ApplianceFactory` and `ApplianceDatabase` are no longer typed.
* CommLib - The following classes are moved to a new package:
	* `CurrentApplianceManager` -> `com.philips.cdp2.commlib.core.appliance`
	* `ApplianceFactory` -> `com.philips.cdp2.commlib.core.appliance`
	* `ApplianceDatabase` -> `com.philips.cdp2.commlib.core.store`
* CommLib - The following classes are renamed:
	* `DICommApplianceFactory` -> `ApplianceFactory`
	* `DICommApplianceDatabase` -> `ApplianceDatabase`
* CommLib - `TransportContext` instances are now contructed with a `RuntimeConfiguration` object to allow for app-specific configuration options
* CommLib - `DiCommPort` no longer has the `isResponseForThisPort` method. This is handled internally by CommLib.
* CommLib - the method `getDICommPortName` has been made public in order to determine the correct destination for incoming port data.

### Defects solved
* CommLib - \#72227 Timeout callback not called
* CommLib - \#73361 XML parser vulnerability during SSDP discovery
* CommLib - \#85583 Use injectable AppInfra
* CommLib - \#73511 Use secure random for encryption (reported by HP Fortify)
* CommLib BLE - \#76117 Let BlueLib make connect() calls without timeout
* CommLib BLE - \#75429 Not registering SHNLogger when app configured having `AppIdentityInterface.AppState.PRODUCTION`
* CloudController - \#55808 BufferUnderflowException while reading download data
* CommLib - \#84930 CommLib responses should include port being subscribed to
	* CommLib now determines for which port incoming data is.
	* Ports are no longer required to determine if JSON matches their port properties.
* CommLib BLE - \#87558 Devices are no longer listed multiple times in device discovery
* CommLib - \#84930 CommLib responses should include port being subscribed to
	* CommLib now determines for which port incoming data is.
	* Ports are no longer required to determine if JSON matches their port properties.
* CommLib - \#91908 NPE in LanCommunicationStrategy fixed. 
* CommLib - \#81027 Subscriptions continue to work when security key changes.



### Residual anomalies
* To be filled in at release

### Risks and mitigations
* N/A

Version 7.0.0
-------------

### Functionality Delivered
* \#35187 Pin HTTPS certificate on first use. At this moment you can't revoke a pinned certificate, so when an appliance gets a new certificate you have to remove all data from your app to let it pin the certificate again.
* \#37438 Component now exposes its version and tla via BuildConfig
* \#35191 Pin mismatch api added
	* `onPortError` will give `Error.INSECURE_CONNECTION` when there is a pin mismatch.
	* `LanTransportContext` received utility methods to handle pin mismatches:
		* `LanTransportContext#rejectNewPinFor`
		* `LanTransportContext#acceptNewPinFor`
		* `LanTransportContext#findAppliancesWithMismatchedPinIn`

### Backwards Compatibility
* Appliances that do not support HTTPS should call networkNode.useLegacyHttp() in their `DICommApplianceFactory.createApplianceForNode(..)` implementation. This is only here for older appliances, newer appliances with HTTPS support should never call this function!

### Features not covered
* To be filled in at release

### Breaking Changes
* `NetworkNode.PAIRED_STATUS` has been renamed to `NetworkNode.PairingState`
* `NetworkNode` no longer extends `Observable`, but adds `PropertyChangeSupport` instead.
* `DICommApplianceFactory#getSupportedModelNames` was changed into `DICommApplianceFactory#getSupportedDeviceTypes` to reflect the renaming of the `NetworkNode` `modelName` property into `deviceType`.

### Defects solved
* NA

### Residual anomalies
* To be filled in at release

### Risks and mitigations
* NA


Version 6.0.0
-------------

### Functionality Delivered
* \#9356 Product quality HTTPS
* \#35182 Subscriptions working for HTTPS nodes

### Backwards Compatibility
* Appliances that do not support HTTPS should call networkNode.useLegacyHttp() in their DICommApplianceFactory.createApplianceForNode(..) implementation. This is only here for older appliances, newer appliances with HTTPS support should never call this function!

### Features not covered
* NA

### Breaking Changes
* NetworkNode.getModelName() and NetworkNode.setModelName(String modelName) are renamed to NetworkNode.getDeviceType() and NetworkNode.setDeviceType(String deviceType)
* See backwards compatibility

### Defects solved
* \#49699 Software Version and ModelId incorrectly required in device port
* \#56704: GetProps requests for LAN now significantly sped up.

### Residual anomalies
* NA

### Risks and mitigations
* NA

Version 5.0.0
-------------

### API Changes
* DICommPort.putProperties(Map<String, Object> dataMap) now uses GSON for JSON conversion: Arrays of types other then String in the dataMap are now correctly processed and converted to JSON. Previously it was possible to add JSON strings to an Array and have them sent unescaped, this is no longer possible.
* The PairingPort has a new pairing call available for user pairing. (#9941)

### New features
* \#9941 User Pairing adds new function to the Pairing port.

Version 4.0.0
-------------

### API Changes
* NetworkNode.getModelType() and NetworkNode.setModelType(String modelType) are renamed to NetworkNode.getModelId() and NetworkNode.setModelId(String modelId)
* FirmwarePort: a timeout value was added as argument to pushLocalFirmware(byte[], long), pullRemoteFirmware(String, long), cancel(long) and deployFirmware(long)
* FirmwarePort: canUpgrade() method was added. The user can use this to determine if the device is able or willing to perform a firmware upgrade.

### New features
* DiscoveryStrategy interface is extended with start(Set<String> deviceTypes, Set<String> modelIds) method to allow filtering on model ids.
 
### Bugs fixed
* 17024 Internet permission is added to CommLib. If you have manifest mergeing enabled you no longer have to specify this permission in your app.

### Known issues

Version 3.1.2
-------------

### API Changes
* Added type parameter to DICommPortListener to prevent unneeded inline casting.
* Added PortProperties marker interface to type parameter of DICommPort in order to enforce ProGuard rules (prevent field name mangling for JSON deserialization).

### New features
* COM-24 Updated to work with new CloudController API
* COM-24 Improved user pairing
* COM-141 Expanded Errors Enum to cover all errors in DiComm spec
* COM-207 New API for discovery (using CommCentral)
* COM-209 BLE discovery
* COM-249 Https support
* CON-119 Firmware update
 
### Bugs fixed
* [DE15085] ProGuard configuration for release builds

### Known issues
* The new API for discovery is not working yet for lan communication. The old DiscoveryManager should be used.

Version 2.0.0
-------------

### New features
* COM-119 DiComm client lib can now be initialized with external CloudController.
* COM-24 Added startUserPairing call to PairingHandler that allows to create user relationship. 

### Bugs fixed

### Known issues

Version 1.2.2
-------------

This release is for testing the Jenkins 2 build pipeline.

### New features

### Bugs fixed

### Known issues

Version v1.2.1
--------------

### New features
* Instead of supplying NetworkNode to every method, NetworkNode parameter is added to construstors (for example see CommunicationMarshal or DISecurity).

### Bugs fixed

### Known issues

Version v1.2.0
--------------

### New features
* DICommAppliance class signature change: 
	disableSubscription() renamed to disableCommunication()
* enableSubscription() renamed to enableCommunication()

### Bugs fixed
* DE12197 Fixed timing issues with SignOn ongoing. 

### Known issues

Version v1.1.3
--------------

### New features
* BG-294 Added impementation for getModelNumber in NetworkNode so it is possible to distinguish between appliance models in ApplianceFactory

### Bugs fixed
* DE12042 Rebooting appliance no longer removes pairing relationships 
* DE11897 When requesting removal of backend relations then the reletions are actually removed  

### Known issues

Version v1.1.2
--------------

### New features
* Upgraded ICPClient to v7.1.0_A.1

### Bugs fixed
* DE11257 Remote subscriptions' reponse messages are now parsed properly. 
* DE11299 DevicePort now supports subscription 

### Known issues

Version v1.1.0
--------------

### New features
* The following fields on the Firmware port are now non optional: name, version and progress.
* When an appliance is disconnected it is not subscribed anymore. As soon as it become connected, the subscription will still occur.
* The interface of PairingListener has changed, now it know the type of appliance, and will return the appliance instead of just its network node on success or failure.

### Bugs fixed

### Known issues

Version v1.0.7
--------------

### New features
* Remote control in sample app

### Bugs fixed
* DE10160: DIComm request error in Android
* DE9705: In Pairing relations, default AllowDelegation must be set to FALSE

### Known issues

Version v1.0.6
--------------

### New features
* Full 64-bit SSDP support
* New ICP client with 64-bit ARM support (v6.1.2_A.2)

### Bugs fixed
* DE9405: Fixed
* DE9406: Fixed
* DE9407: Not a bug. Use getSchedules to obtain the numbers.
* DE9719: Fixed

### Known issues

Version v1.0.5
--------------

### New features

### Bugs fixed
* Removed incorrect loging in SchedulelLstPort

### Known issues

Version v1.0.4
--------------

### New features
* Enabled remote control, by removing Airpurifier specific CPP code
* Improved data handling recieved from remote network location

### Bugs fixed
* Reworked ScheduleListPort for remote communication

### Known issues

Version 1.0.3
-------------

### New features
* Added command property to ScheduleListPort
* Enabled name functionality on schedules
* Improved data paring of device port
* Updated dependenies

### Bugs fixed
* DE8889: Not a bug
* DE8863: Fixed

### Known issues
