/*
@startuml
Mobile -> Moonshine : rxMsg(type=start, seqNo=0)
Moonshine -> Mobile : rxAck(seqNo=0)
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=1)
... seqNo = 0 ...
loop N times |  N > 0 && N < rxWindow - 1
    Mobile -> Moonshine : rxMsg(type=data, seqNo=x)
end
Mobile -> Moonshine : rxMsg(type=data, seqNo=N)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=N + 1)
... seqNo = 0 ...
loop N times |  N = rxWindow - 1
    Mobile -> Moonshine : rxMsg(type=data, seqNo=x)
end
Mobile -> Moonshine : rxMsg(type=data, seqNo=rxWindow)
Moonshine -> Mobile : rxAck(seqNo=rxWindow)
... seqNo = 0 ...
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
... receive data timeout ...
Moonshine --> Mobile : rxAck(seqNo=1)
... receive ack timeout ...
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=1)
... seqNo = 0 ...
Mobile --> Moonshine : rxMsg(type=data, seqNo=1)
... receive ack timeout ...
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=1)
... seqNo = 0 ...
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
Mobile -> Moonshine : rxMsg(type=data, seqNo=2)
Mobile --> Moonshine : rxMsg(type=data, seqNo=3)
Mobile -> Moonshine : rxMsg(type=data, seqNo=4)
Mobile -> Moonshine : rxMsg(type=data, seqNo=5)
... receive ack timeout ...
Mobile -> Moonshine : rxMsg(type=data, seqNo=1)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=2)
Mobile -> Moonshine : rxMsg(type=data, seqNo=3)
Mobile -> Moonshine : rxMsg(type=data, seqNo=4)
Mobile -> Moonshine : rxMsg(type=data, seqNo=5)
... receive data timeout ...
Moonshine -> Mobile : rxAck(seqNo=5)
@enduml

@startuml
[*] --> CtrlStopped
CtrlStopped --> CtrlCollecting : startButton/\nresetResults\nselectDevice\nstart(dev)
CtrlCollecting --> CtrlCollecting : devCollected/\nselectDevice\nstart(dev)
CtrlCollecting --> CtrlStopped : devCollected/\nselectDevice
CtrlCollecting --> CtrlStopping : stopButton/\nstop
CtrlStopping --> CtrlStopped : devCollected

state CtrlCollecting {
[*] --> Idle
Idle --> Connecting : start/\nsetDevDelegate\nconnect\nstartTimer
Connecting --> Collecting : connected/\nstopTimer\nsetCapDelegate\nstartLogSync
Collecting --> Collecting : progress/\nupdateProgress
Collecting --> Disconnecting : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegete\ndisconnect
Disconnecting --> [*] : disconnected/\nfreeDevDelegate\ndevCollected(log,result)

' Timeout while connecting
Connecting --> [*] : timeout/\nfreeDevDelegate\ndevCollected(log,result)

' Handle stop
Connecting --> Disconnecting : stop/\nstopTimer\ndisconnect
Collecting --> WaitForDisconnectedAndSyncDone : stop/\ndisconnect
WaitForDisconnectedAndSyncDone --> WaitForDisconnectedAndSyncDone : progress/\nupdateProgress
WaitForDisconnectedAndSyncDone --> WaitForSyncDone : disconnected/\nfreeDevDelegate
WaitForDisconnectedAndSyncDone --> Disconnecting : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegate
WaitForSyncDone --> WaitForSyncDone : progress/\nupdateProgress
WaitForSyncDone --> [*] : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegate\ndevCollected(log,result)

' Handle unexpected disconnects
Connecting --> [*] : disconnected/\nstopTimer\nupdateResult\nfreeDevDelegate\ndevCollected(log,result)

Collecting --> WaitForSyncDone : disconnected/\nfreeDevDelegate
}

@enduml
@startuml
[*] --> Stopped
' Normal flow
Stopped --> Stopped : start+noDevices/\nresetResults\nresetState\nselectDevice
Stopped --> Connecting : start/\nresetResults\nresetState\nselectDevice\nsetDevDelegate\nconnect\nstartTimer
Connecting --> Collecting : connected/\nstopTimer\nsetCapDelegate\nstartLogSync
Collecting --> Collecting : progress/\nupdateProgress
Collecting --> Disconnecting : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegete\ndisconnect
Disconnecting --> Connecting : disconnected+nextDev/\nfreeDevDelegate\nselectDevice\nsetDevDelegate\nconnect\nstartTimer
Disconnecting --> Stopped : disconnected+alldevDone/\nfreeDevDelegate\nselectDevice

' Timeout while connecting
Connecting --> Connecting : timeout+nextDev/\nfreeDevDelegate\nupdateResult\ndisconnect\nselectDevice\nsetDevDelegate\nconnect\nstartTimer
Connecting --> Stopped : timeout+alldevDone/\nfreeDevDelegate\nupdateResult\ndisconnect\nselectDevice

' Handle stop
Connecting --> WaitForDisconnectedAndSyncDone2 : stop/\ndisconnect
Collecting --> WaitForDisconnectedAndSyncDone1 : stop/\ndisconnect
WaitForDisconnectedAndSyncDone1 --> WaitForDisconnectedAndSyncDone1 : progress/\nupdateProgress
WaitForDisconnectedAndSyncDone1 --> WaitForDisconnectedAndSyncDone2 : disconnected/\nfreeDevDelegate
WaitForDisconnectedAndSyncDone1 --> WaitForDisconnectedAndSyncDone2 : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegate
WaitForDisconnectedAndSyncDone2 --> WaitForDisconnectedAndSyncDone2 : progress/\nupdateProgress
WaitForDisconnectedAndSyncDone2 --> Stopped : disconnected/\nfreeDevDelegate
WaitForDisconnectedAndSyncDone2 --> Stopped : syncDone/\nstoreLog\nupdateResult\nfreeCapDelegate

' Handle unexpected disconnects
Connecting --> Connecting : disconnected+nextDev/\nupdateResult\nfreeDevDelegate\nselectDevice\nsetDevDelegate\nconnect\nstartTimer
Connecting --> Stopped : disconnected+alldevDone/\nupdateResult\nfreeDevDelegate\nselectDevice

Collecting --> WaitForSyncDone : disconnected/\nfreeDevDelegate
WaitForSyncDone --> WaitForSyncDone : progress/\nupdateProgress
WaitForSyncDone --> Connecting : syncDone+nextDev/\nstoreLog\nupdateResult\nfreeCapDelegate\nselectDevice\nsetDevDelegate\nconnect\nstartTimer
WaitForSyncDone --> Stopped : syncDone+allDevDone/\nstoreLog\nupdateResult\nfreeCapDelegate\nselectDevice

@enduml
@startuml
[*] --> Stopped
Stopped --> Started : start/clearAllProgress
Started --> Stopped : done
state Started {
    [*] --> Idle
    Idle --> [*] : stop/done
    Idle --> [*] : allDevicesDone/done
    Idle --> Connecting : nextDeviceSelected/connect
    Connecting --> Connected : connected/startLogSync
    Connecting --> Idle : timeout/reportDeviceUnavailable
    Connecting --> [*] : stop/cancel connect
    Connected --> Collecting : logSyncStarted/
    Connected --> WaitForCancelConfirm : stop/cancelLogSync
    WaitForCancelConfirm --> [*] : cancelConfirm/
    Collecting --> Collecting : progressUpdate/reportProgress
    Collecting --> Idle : onLogSynchronized/reportResult
    Collecting --> Idle : onLogSynchronizationFailed/reportResult
    Collecting --> WaitForCancelConfirm : stop/cancelLogSync
}
@enduml

@startuml
[*] --> Idle
Idle --> Synchronizing : startSynchronizationFromToken / setReceiveMeasurements, updateProgress(0), restartTimer
Synchronizing --> Idle : setReceiveTemperatureMeasurementsCompletionError / finishLoggingResult(error)
Synchronizing --> Idle :
@enduml

 * @startuml
class SHNCentral {
    - Handler userCallbackHandler
    - ScheduledThreadPoolExecutor bluetoothExecutor
    + Handler getUserCallbackHandler()
    + ScheduledThreadPoolExecutor getBluetoothExecutor()
    + void registerDeviceDefinitionInfo()
}
note as NoteOnSHNCentral
    All bluetooth calls are made from a one
    thread ScheduledThreadPoolExecutor. Results
    are communicated to the user via the user
    provided Handler (or the UI Handler when
    none is given).
    This also solves the problem that on Android
    all bluetooth calls must be made from the same
    thread and bluetooth callbacks should execute
    as fast as possible (collecting only some data).
end note
interface SHNDeviceDefinitionInfo {
    + String getDeviceTypeName()
    + Set<UUID> getPrimaryServiceUUIDs()
    + SHNDeviceAssociation getShnDeviceAssociation()
    + SHNDeviceDefinition createNewInstance()
}
interface SHNDeviceDefinition {
    + Set<SHNCapability> getExposedCapabilities()
    + SHNDevice createDeviceFromPeripheralIdentifier()
    + void associateWithCompletion()
}
enum SHNDeviceScannerState {
    IDLE
    SCANNING
}
interface SHNDeviceScannerListener {
    + void deviceFound()
    + void scanStopped()
}
class SHNDeviceScanner {
    - SHNDeviceScannerListener shnDeviceScannerListener
    - SHNDeviceScannerState state
    + boolean startScanning()
    + void stopScanning()
}
interface LeScanCallback {
    + void onLeScan()
}
class LeScanCallbackProxy

NoteOnSHNCentral .. SHNCentral
SHNCentral "1" *-- "many" SHNDeviceDefinitionInfo
SHNDeviceScanner "many" o-- "1" SHNCentral
LeScanCallback <|-- SHNDeviceScanner : implements <

class SHNDevice {
}
class SHNService {
}
interface SHNServiceListener {
}
class SHNCapability {
}

SHNDevice "1" *-- "many" SHNService
SHNDevice "1" *-- "many" SHNCapability
SHNServiceDeviceInformation --|> SHNServiceListener : implements
SHNServiceDeviceInformation --|> SHNService : implements
SHNCapabilityDeviceInformation --|> SHNCapability

SHNCapabilityHeartRate --o SHNServiceHeartRate
SHNCapabilityHeartRate --|> SHNServiceListenerHeartRate
SHNServiceHeartRate --|> SHNService
 * @enduml
 *
 * @startuml
interface SHNDataStreaming {
}
class SHNCapabilityHeartRate {
}
class SHNServiceHeartRate {
}
class SHNService {
}
class SHNServiceListenerHeartRate {
}
class SHNServiceListener {
}

SHNDataStreaming <|-- SHNCapabilityHeartRate
SHNCapabilityHeartRate --> SHNServiceHeartRate
SHNServiceHeartRate --> SHNService
SHNService -right-> SHNServiceListener
SHNServiceHeartRate -right-> SHNServiceListenerHeartRate
SHNServiceListenerHeartRate --|> SHNServiceListener
SHNCapabilityHeartRate --|> SHNServiceListenerHeartRate
SHNService "1" *-- "many" SHNCharacteristic
 * @enduml
 *
 * @startuml

note as NoteHeartRateConstants
    ""ServiceUUID                            0x180D""
    ""HeartRateMeasurementCharacteristicUUID 0x2A37""
    ""BodySensorLocationCharacteristicUUID   0x2A38""
end note
SHNDeviceDefinition <|-- SHNDeviceDefinitionHeartRateSensor
SHNCapabilityDataStreamingHeartRateSensor <|-- SHNCapabilityDataStreaming
SHNServiceHeartRate *-- SHNService
SHNServiceHeartRate o-- SHNServiceHeartRateListener
class SHNServiceHeartRate {
    + SHNServiceHeartRate(SHNDevice shnDevice)
    + setMeasurementEnabled(boolean enabled)
}
interface SHNServiceHeartRateListener {
    + void onChangeMeasurementEnabled(boolean enabled)
    + void onReceiveHeartRateMeasurement(int heartRate)
}

 * @enduml
 *
 * @startuml

note as NoteHeartRateConstants
    ""ServiceUUID                            0x180D""
    ""HeartRateMeasurementCharacteristicUUID 0x2A37""
    ""BodySensorLocationCharacteristicUUID   0x2A38""
end note

class SHNService {
    + SHNService(\n    SHNDevice shnDevice,\n    UUID serviceUUID,\n    Set<UUID> requiredCharacteristics,\n    Set<UUID> optionalCharacteristics)
    + SHNCharacteristic getCharacteristicByUUID(UUID uuid)
}
interface SHNServiceListener {
    + void onServiceChangedState(\n    SHNService shnService,\n    SHNServiceState shnServiceState)
}
interface SHNCharacteristicListener {
    + void onUpdateValue(SHNCharacteristic shnCharacteristic, int error)
    + void onUpdateNotificationState(SHNCharacteristic shnCharacteristic, int error)
}
SHNService "1" *-- "many" SHNCharacteristic
SHNService <|-- SHNServiceHeartRate
SHNServiceListener <|-- SHNServiceHeartRate
SHNCharacteristicListener <|-- SHNServiceHeartRate
SHNServiceHeartRate o-- SHNServiceHeartRateListener
class SHNServiceHeartRate {
    + SHNServiceHeartRate(SHNDevice shnDevice)
    + setMeasurementEnabled(boolean enabled)
}
interface SHNServiceHeartRateListener {
    + void onUpdateMeasurementEnabled(boolean enabled)
    + void onReceiveMeasurement(int heartRate)
}
 * @enduml
 *
 * @startuml
actor App
participant SHNCentral
participant SHNDeviceScanner
entity DeviceDefinition

activate App
activate DeviceDefinition

App --> SHNCentral : << create >>
activate SHNCentral

App -> SHNCentral : registerDeviceDefinition(DeviceDefinition)

App -> SHNCentral : getDeviceScanner
SHNCentral <-> DeviceDefinition : getPrimaryServiceUUIDs

SHNCentral --> SHNDeviceScanner : << create >>
activate SHNDeviceScanner

App -> SHNDeviceScanner : startScanning(scanCallback)
SHNDeviceScanner --> SHNDevice : << createWhenFound >>
activate SHNDevice
SHNDeviceScanner --> App : scanCallback(shnDevice)
App -> SHNDevice : registerSHNDeviceListener(deviceCallback)
App -> SHNDevice : connect

SHNDevice --> App : deviceCallback(Connecting)

SHNDevice -> DeviceDefinition : createDeviceFromDefinition

DeviceDefinition -> SHNCapabilityDeviceInformation : << create >>
activate SHNCapabilityDeviceInformation

DeviceDefinition -> SHNService : << create >>
activate SHNService

SHNService -> SHNCharacteristic : << create >>
activate SHNCharacteristic

SHNDevice --> App : deviceCallback(Connected)

App -> SHNDevice : getSupportedCapabilityTypes
App -> SHNDevice : getCapabilityForType(DI)
App -> SHNCapabilityDeviceInformation : readDeviceInformationType(type, diCallback)
SHNCapabilityDeviceInformation --> App : diCallback
 * @enduml
 * @startuml

interface SHNCapability {
}

class SHNCapabilityDeviceInformation {
}

class SHNDeviceDefinition {
}

class SHNDevice {
    - BluetoothDevice bluetoothDevice
    + connect()
    + disconnect()
}

class SHNService {
    - BluetoothGattService bluetoothGattService
}

class SHNCharacteristic {
    - BluetoothGattCharacteristic bluetoothGattCharacteristic
}

class SHNDeviceListener {
}

class App {
}

SHNCapability <|-- SHNCapabilityDeviceInformation
SHNDevice "1" *-- "1" SHNDeviceDefinition
App "1" *-- "*" SHNDevice
SHNDevice "1" o-- "1" SHNDeviceListener
SHNDeviceDefinition "1" o-- "*" SHNCapability
SHNDeviceDefinition "1" *-- "*" SHNService
SHNService "1" *-- "*" SHNCharacteristic
 * @enduml
 *
 * @startuml
title Device Association
class  App
class SHNDeviceAssociation
class SHNDeviceDefinitions
interface SHNScanningListener
interface SHNAssociationProcedure
interface SHNAssociationProcedureListener
interface SHNDeviceDefinition
class SHNAssociationProcedureNearestDevice
class MoonshineDeviceDefinition
App -> SHNDeviceAssociation
App --> SHNAssociationProcedureNearestDevice
SHNDeviceAssociation -up-|> SHNScanningListener
SHNDeviceAssociation --|> SHNAssociationProcedureListener
SHNDeviceAssociation -> SHNDeviceDefinitions
SHNDeviceAssociation --> SHNDeviceDefinition
SHNDeviceAssociation -down-> SHNAssociationProcedure
SHNDeviceDefinitions *-down- SHNDeviceDefinition
SHNAssociationProcedureNearestDevice -up-|> SHNAssociationProcedure
SHNAssociationProcedureNearestDevice -up-> SHNAssociationProcedureListener
MoonshineDeviceDefinition -up-|> SHNDeviceDefinition
MoonshineDeviceDefinition .left.> SHNAssociationProcedureNearestDevice
 * @enduml
 * @startuml
title Scanning
class SHNDeviceScanner
class SHNDeviceAssociation
class SHNCentral
interface SHNDeviceScannerListener
SHNDeviceScanner -up-|> SHNDeviceScannerListener
SHNDeviceAssociation -up-|> SHNDeviceScannerListener
SHNDeviceScanner -down-> SHNCentral
SHNDeviceAssociation -down-> SHNCentral
SHNCentral -> SHNDeviceScannerListener
 * @enduml
 */
