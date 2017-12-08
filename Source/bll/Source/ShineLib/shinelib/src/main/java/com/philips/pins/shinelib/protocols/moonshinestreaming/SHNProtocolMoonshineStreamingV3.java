/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.protocols.moonshinestreaming;
/*
@startuml
participant PUser
participant Protocol
participant Service
participant RxChar
participant RxAckChar
participant TxChar
participant TxAckChar
participant CfgChar
group initialization
    Service -> Protocol : onServiceAvailable
    Protocol -> Service : readProtocolConfiguration
    group if optional CfgChar available
        Service -> CfgChar : readCharacteristic
        CfgChar -> Service : onCharacteristicRead
    end
    group if optional CfgChar unavailable
        Service -> Service : onCharacteristicRead
    end
    Service -> Protocol : onReadProtocolInformation
    loop until onReceiveAck(sn=0) is received or max attempts done
        Protocol -> Service : sendData(startMsg,sn=0)
        Service -> RxChar : writeCharacteristic(dataChunk,sn=0)
        RxChar --> Service : onCharacteristicWrite
        RxAckChar -> Service : onCharacteristicChanged(sn=0)
        Service --> Protocol : onReceiveAck(sn=0)
    end
    Protocol -> Service : upperlayerReady
    note right: This call is needed to allow the service to transition to the Ready state.
    Protocol -> PUser : onProtocolAvailable
end
@enduml
@startuml
participant PUser
participant Protocol
participant Service
participant RxChar
participant RxAckChar
participant TxChar
participant TxAckChar
participant CfgChar
group send 7 dataChunks window=3
    PUser -> Protocol : sendData
    Protocol -> Service : sendData(dataChunk,sn=1)
    Service -> RxChar : writeCharacteristic(dataChunk,sn=1)
    Protocol -> Service : sendData(dataChunk,sn=2)
    Protocol -> Service : sendData(dataChunk,sn=3)
    RxChar --> Service : onCharacteristicWrite
    Service -> RxChar : writeCharacteristic(dataChunk,sn=2)
    RxChar --> Service : onCharacteristicWrite
    Service -> RxChar : writeCharacteristic(dataChunk,sn=3)
    RxChar --> Service : onCharacteristicWrite
    RxAckChar -> Service : onCharacteristicChanged(sn=3)
    note left: timeout: resend chunk1,2 and 3\nAck(1) resend chunk 2 and 3\nAck(2) resend chunk 3
    Service -> Protocol : onReceiveAck(sn=3)
    Protocol -> Service : sendData(dataChunk,sn=4)
    Service -> RxChar : writeCharacteristic(dataChunk,sn=4)
    Protocol -> Service : sendData(dataChunk,sn=5)
    Protocol -> Service : sendData(dataChunk,sn=6)
    RxChar --> Service : onCharacteristicWrite
    Service -> RxChar : writeCharacteristic(dataChunk,sn=5)
    RxAckChar -> Service : onCharacteristicChanged(sn=4)
    Service -> Protocol : onReceiveAck(sn=4)
    Protocol -> Service : sendData(dataChunk,sn=7)
    RxChar --> Service : onCharacteristicWrite
    Service -> RxChar : writeCharacteristic(dataChunk,sn=6)
    RxChar --> Service : onCharacteristicWrite
    Service -> RxChar : writeCharacteristic(dataChunk,sn=7)
    RxChar --> Service : onCharacteristicWrite
    RxAckChar -> Service : onCharacteristicChanged(sn=7)
    Service -> Protocol : onReceiveAck(sn=7)
end
@enduml
@startuml
participant PUser
participant Protocol
participant Service
participant RxChar
participant RxAckChar
participant TxChar
participant TxAckChar
participant CfgChar
group receive 5 dataChunks window=2
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=1)
    Service -> Protocol : onReceiveData(dataChunk,sn=1)
    Protocol -> PUser : onDataReceived(dataChunk)
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=2)
    Service -> Protocol : onReceiveData(dataChunk,sn=2)
    Protocol -> PUser : onDataReceived(dataChunk)
    ...
    Protocol --> Service : sendAck(sn=2)
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=1)
    Service -> Protocol : onReceiveData(dataChunk,sn=1)
    note left: Discarded because it is already sent
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=2)
    Service -> Protocol : onReceiveData(dataChunk,sn=2)
    note left: Discarded because it is already sent
    ...
    Protocol -> Service : sendAck(sn=2)
    Service -> TxAckChar : writeCharacteristic(sn=2)
    TxAckChar --> Service : onCharacteristicWrite
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=3)
    Service -> Protocol : onReceiveData(dataChunk,sn=3)
    Protocol -> PUser : onDataReceived(dataChunk)
    ...
    Protocol -> Service : sendAck(sn=3)
    Service -> TxAckChar : writeCharacteristic(sn=3)
    TxAckChar --> Service : onCharacteristicWrite
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=4)
    Service -> Protocol : onReceiveData(dataChunk,sn=4)
    Protocol -> PUser : onDataReceived(dataChunk)
    TxChar -> Service : onCharacteristicChanged(dataChunk,sn=5)
    Service -> Protocol : onReceiveData(dataChunk,sn=5)
    Protocol -> PUser : onDataReceived(dataChunk)
end
@enduml
@startuml
[*] --> Initializing
Initializing --> AcquiringConfiguration : onServiceAvailable / readProtocolConfiguration
AcquiringConfiguration --> WaitingForHandshakeAck : onConfigurationRead / sendData(startMsg,sn=0) startReceiveAckTimer
WaitingForHandshakeAck --> Error : ReceiveAckTimeout
AcquiringConfiguration --> Error : onReadProtocolInformationWithUnsuportedProtocolVersion
WaitingForHandshakeAck --> Ready : onReceiveAck / stopReceiveAckTimer
Ready --> Ready : sendData
Ready --> Ready : OnReceiveAck
Ready --> Error : ReceiveAckTimeout

state Ready {
    [*] --> Idle
    Idle --> WaitForAck : sendData / startReceiveAckTimer
    WaitForAck --> SendWindowFull : sendData
    SendWindowFull --> WaitForAck : onReceiveAck / restartReceiveAckTimer
    WaitForAck --> Idle : onReceiveAck / stopReceiveAckTimer
}

AcquiringConfiguration --> Initializing : onServiceUnavailable
WaitingForHandshakeAck --> Initializing : onServiceUnavailable
Ready --> Initializing : onServiceUnavailable
Error --> Initializing : onServiceUnavailable
@enduml
 */

import android.os.Handler;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceByteStreaming;
import com.philips.pins.shinelib.services.SHNServiceMoonshineStreaming;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.Utilities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SHNProtocolMoonshineStreamingV3 implements SHNProtocolMoonshineStreaming, SHNServiceMoonshineStreaming.SHNServiceMoonshineStreamingListener {
    private static final boolean ENABLE_DEBUG_LOGGING = false;
    private static final String TAG = SHNProtocolMoonshineStreamingV3.class.getSimpleName();

    public static final int DEFAULT_TX_WINDOW_SIZE = 12;
    public static final int PROTOCOL_VERSION = 3;
    public static final long RECEIVE_ACK_TIMEOUT = 5000L;
    public static final long RECEIVE_NEXT_MSG_TIMEOUT = 30L;
    public static final int MAX_PAYLOAD_SIZE = 19;
    public static final int MAX_TX_RETRIES = 3;

    private final SHNServiceByteStreaming shnServiceByteStreaming;
    private final Handler internalHandler;
    private SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener;
    private SHNProtocolMoonshineStreamingState state = SHNProtocolMoonshineStreamingState.Initializing;
    private SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate substate = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.Idle;

    public static final int MAX_SEQUENCE_NR = 0x40; // Actually the max seqnumber used is one lower than this constant. Modulus calculation is used.
    public static final int HEADER_TYPE_MASK = 0xC0;
    public static final int HEADER_SEQNR_MASK = 0x3F;
    public static final int HEADER_PACKET_TYPE_START = 0x40;

    private int txWindowSize;
    private int txLastGenSequenceNr = -1;
    private int txLastSendSequenceNr = -1;
    private int txLastAckedSequenceNr = -1;
    private int rxWindowSize;
    private int rxNextExpectedSequenceNr = 0;
    private int rxLastReceivedSequenceNr = -1;
    private int rxLastAckedSequenceNr = -1;
    private Queue<byte[]> toBeSendPacketQueue;
    private Queue<byte[]> waitForAckPacketQueue;
    private Runnable txAckTimer;
    private Runnable rxAckTimer;
    private int txResendWithoutProgressCount;

    public SHNProtocolMoonshineStreamingV3(SHNServiceByteStreaming shnServiceByteStreaming, Handler internalHandler) {
        DebugLog("create instance");
        this.shnServiceByteStreaming = shnServiceByteStreaming;
        this.internalHandler = internalHandler;
        toBeSendPacketQueue = new LinkedList<>();
        waitForAckPacketQueue = new LinkedList<>();
        setInitialState();
    }

    public SHNProtocolMoonshineStreamingListener getShnProtocolMoonshineStreamingListener() {
        DebugLog("getShnProtocolMoonshineStreamingListener");
        return shnProtocolMoonshineStreamingListener;
    }

    public SHNProtocolMoonshineStreamingState getState() {
        DebugLog("getState");
        return state;
    }

    public int getTxWindowSize() {
        DebugLog("getTxWindowSize");
        return txWindowSize;
    }

    public int getRxWindowSize() {
        DebugLog("getRxWindowSize");
        return rxWindowSize;
    }

    @Override
    public void setShnProtocolMoonshineStreamingListener(SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener) {
        DebugLog("setShnProtocolMoonshineStreamingListener");
        this.shnProtocolMoonshineStreamingListener = shnProtocolMoonshineStreamingListener;
    }

    @Override
    public void sendData(byte[] data) {
        DebugLog("sendData");
        int start = 0;
        int length = data.length;
        while (length > 0) {
            int packetSize = length;
            if (packetSize > MAX_PAYLOAD_SIZE) {
                packetSize = MAX_PAYLOAD_SIZE;
            }
            byte[] packet = createPacket(SHNProtocolByteStreamingVersionSwitcher.PacketType.DATA, data, start, packetSize);
            queueTxPacketAndSend(packet);
            start += packetSize;
            length -= packetSize;
        }
    }

    @Override
    public void transitionToError(SHNResult shnResult) {
        state = SHNProtocolMoonshineStreamingState.Error;
        shnServiceByteStreaming.transitionToError();
    }

    @Override
    public void transitionToReady() {
        shnServiceByteStreaming.transitionToReady();
    }

    private void setInitialState() {
        DebugLog("setInitialState");
        state = SHNProtocolMoonshineStreamingState.Initializing;
        substate = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.Idle;
        stopRxAckTimer();
        stopTxAckTimer();
        txWindowSize = 0;
        txLastGenSequenceNr = -1;
        txLastSendSequenceNr = -1;
        txLastAckedSequenceNr = -1;
        txResendWithoutProgressCount = MAX_TX_RETRIES;
        rxWindowSize = 0;
        rxNextExpectedSequenceNr = 0;
        rxLastReceivedSequenceNr = -1;
        rxLastAckedSequenceNr = -1;
        toBeSendPacketQueue.clear();
    }

    private int calcPacketsInRxWindow() {
        return ((MAX_SEQUENCE_NR + rxLastReceivedSequenceNr) - rxLastAckedSequenceNr) % MAX_SEQUENCE_NR;
    }

    private byte generateNextTxSequenceNr() {
        txLastGenSequenceNr += 1;
        txLastGenSequenceNr %= MAX_SEQUENCE_NR;
        return (byte) txLastGenSequenceNr;
    }

    private boolean isTxSeqNrInsideWindow(int acknowledgeSequenceNr) {
        if (txLastAckedSequenceNr <= txLastSendSequenceNr) {
            if (txLastAckedSequenceNr < acknowledgeSequenceNr && acknowledgeSequenceNr <= txLastSendSequenceNr) {
                return true;
            }
        } else {
            if (txLastAckedSequenceNr < acknowledgeSequenceNr || acknowledgeSequenceNr <= txLastSendSequenceNr) {
                return true;
            }
        }
        return false;
    }

    private void updateTxSlidingWindowAndSubstate(int acknowledgeSequenceNr) {
        while (txLastAckedSequenceNr != acknowledgeSequenceNr) {
            txResendWithoutProgressCount = MAX_TX_RETRIES;
            waitForAckPacketQueue.remove();
            txLastAckedSequenceNr++;
            txLastAckedSequenceNr %= MAX_SEQUENCE_NR;
        }
        updateTxSubstate();
    }

    private void updateTxSubstate() {
        if (txLastAckedSequenceNr == txLastSendSequenceNr) {
            substate = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.Idle;
            stopTxAckTimer();
        } else {
            int packetsInWindow = ((txLastSendSequenceNr - txLastAckedSequenceNr) + MAX_SEQUENCE_NR) % MAX_SEQUENCE_NR;
            if (packetsInWindow == txWindowSize) {
                substate = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.SendWindowFull;
            } else {
                substate = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.WaitForAck;
            }
            restartTxAckTimer();
        }
    }

    private void queueTxPacketAndSend(byte[] packet) {
        DebugLog("queueTxPacketAndSend: " + " [" + Utilities.byteToString(packet) + "]");
        toBeSendPacketQueue.add(packet);
        sendQueuedTxPacketsInWindow();
    }

    private void sendQueuedTxPacketsInWindow() {
        while (substate != SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingSubstate.SendWindowFull && !toBeSendPacketQueue.isEmpty()) {
            byte[] nextTxPacket = toBeSendPacketQueue.remove();
            waitForAckPacketQueue.add(nextTxPacket);
            sendDataToService(nextTxPacket);
            txLastSendSequenceNr = (nextTxPacket[0] & HEADER_SEQNR_MASK);
            updateTxSubstate();
        }
    }

    private void sendDataToService(byte[] txPacket) {
        DebugLog("sending: " + Utilities.byteToString(txPacket));
        shnServiceByteStreaming.sendData(txPacket);
    }

    private void resendWaitingForAckPackets() {
        if (--txResendWithoutProgressCount > 0) {
            DebugLog("Resending " + waitForAckPacketQueue.size() + " packets in the wating for Ack queue");
            Queue<byte[]> tmpResendQueue = new LinkedList<>();

            tmpResendQueue.addAll(waitForAckPacketQueue);
            while (!tmpResendQueue.isEmpty()) {
                byte[] nextTxPacket = tmpResendQueue.remove();
                sendDataToService(nextTxPacket);
            }
        } else {
            SHNLogger.e(TAG, "Retries exceeded: going to error state");
            state = SHNProtocolMoonshineStreamingState.Error;
        }
    }

    private void sendStartMessage() {
        SHNLogger.d(TAG, "sendStartMessage");
        byte[] packet = createPacket(SHNProtocolByteStreamingVersionSwitcher.PacketType.START, null, 0, 0);
        queueTxPacketAndSend(packet);
        state = SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck;
    }

    private byte[] createPacket(SHNProtocolByteStreamingVersionSwitcher.PacketType packetType, byte[] data, int start, int packetSize) {
        byte[] packet;
        if (data != null) {
            packet = new byte[packetSize + 1];
            for (int index = 0; index < packetSize; index++) {
                packet[index + 1] = data[index + start];
            }
        } else {
            packet = new byte[1];
        }
        packet[0] = (byte) (((packetType == SHNProtocolByteStreamingVersionSwitcher.PacketType.START) ? HEADER_PACKET_TYPE_START : 0) + generateNextTxSequenceNr());
        return packet;
    }

    private void restartTxAckTimer() {
        DebugLog("restartTxAckTimer");
        stopTxAckTimer();
        txAckTimer = new Runnable() {
            @Override
            public void run() {
                handleTxAckTimeout();
            }
        };
        internalHandler.postDelayed(txAckTimer, RECEIVE_ACK_TIMEOUT);
    }

    private void stopTxAckTimer() {
        DebugLog("stopTxAckTimer");
        if (txAckTimer != null) {
            internalHandler.removeCallbacks(txAckTimer);
            txAckTimer = null;
        }
    }

    private void handleTxAckTimeout() {
        SHNLogger.d(TAG, "handleTxAckTimeout");
        resendWaitingForAckPackets();
        restartTxAckTimer();
    }

    private void restartRxAckTimer() {
        stopRxAckTimer();
        DebugLog("restartRxAckTimer");
        rxAckTimer = new Runnable() {
            @Override
            public void run() {
                handleRxAckTimeout();
            }
        };
        internalHandler.postDelayed(rxAckTimer, RECEIVE_NEXT_MSG_TIMEOUT);
    }

    private void stopRxAckTimer() {
        DebugLog("stopRxAckTimer");
        if (rxAckTimer != null) {
            internalHandler.removeCallbacks(rxAckTimer);
            rxAckTimer = null;
        }
    }

    private void handleRxAckTimeout() {
        DebugLog("handleRxAckTimeout");
        ackAllRxPackets();
    }

    private void ackAllRxPackets() {
        byte[] data = {(byte) rxLastReceivedSequenceNr};
        DebugLog("ackAllRxPackets: " + Utilities.byteToString(data));
        shnServiceByteStreaming.sendAck(data);
        rxLastAckedSequenceNr = rxLastReceivedSequenceNr;
    }

    // implements SHNServiceMoonshineStreaming.SHNServiceMoonshineStreamingListener
    @Override
    public void onReadProtocolInformation(byte[] data) {
        SHNLogger.d(TAG, "onReadProtocolInformation");
        SHNProtocolByteStreamingVersionSwitcher.SHNProtocolInformation shnProtocolInformation = SHNProtocolByteStreamingVersionSwitcher.SHNProtocolInformation.createFromData(data);
        if (shnProtocolInformation == null || shnProtocolInformation.protocolVersion == PROTOCOL_VERSION) {
            if (shnProtocolInformation != null) {
                txWindowSize = shnProtocolInformation.txWindowSize;
                rxWindowSize = shnProtocolInformation.rxWindowSize;
            } else {
                txWindowSize = DEFAULT_TX_WINDOW_SIZE;
                rxWindowSize = DEFAULT_TX_WINDOW_SIZE; // DEFAULT RX and TX Window size are equal...
            }
            sendStartMessage();
        } else {
            state = SHNProtocolMoonshineStreamingState.Error;
        }
    }

    @Override
    public void onReceiveData(byte[] data) {
        DebugLog("onReceiveData: " + Utilities.byteToString(data));
        if (state == SHNProtocolMoonshineStreamingState.Ready && data != null && data.length > 0) {
            int packetSeqNr = data[0] & HEADER_SEQNR_MASK;
            if (packetSeqNr == rxNextExpectedSequenceNr) {
                rxLastReceivedSequenceNr = packetSeqNr;
                rxNextExpectedSequenceNr++;
                rxNextExpectedSequenceNr %= MAX_SEQUENCE_NR;

                if (shnProtocolMoonshineStreamingListener != null && data.length > 1) {
                    shnProtocolMoonshineStreamingListener.onDataReceived(Arrays.copyOfRange(data, 1, data.length));
                }
                if (calcPacketsInRxWindow() == rxWindowSize) {
                    stopRxAckTimer();
                    ackAllRxPackets();
                } else {
                    restartRxAckTimer();
                }
            } else {
                SHNLogger.e(TAG, "packet dropped seqNr mismatch. actual: " + packetSeqNr + " expected: " + rxNextExpectedSequenceNr);
                restartRxAckTimer();
            }
        } else {
            SHNLogger.e(TAG, "packet dropped state: " + state + " data: " + Utilities.byteToString(data));
        }
    }

    @Override
    public void onReceiveAck(byte[] data) {
        DebugLog("onReceiveAck: " + Utilities.byteToString(data));
        if (SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck == state && data != null && data.length == 1 && data[0] != HEADER_PACKET_TYPE_START) {
            return;
        }

        if ((state == SHNProtocolMoonshineStreamingState.Ready || state == SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck) && data != null && data.length == 1) {
            int acknowledgeSequenceNr = data[0] & HEADER_SEQNR_MASK;
            if (isTxSeqNrInsideWindow(acknowledgeSequenceNr)) {
                updateTxSlidingWindowAndSubstate(acknowledgeSequenceNr);
            }
            if (state == SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck) {
                state = SHNProtocolMoonshineStreamingState.Ready;
                if (shnProtocolMoonshineStreamingListener != null) {
                    shnProtocolMoonshineStreamingListener.onProtocolAvailable();
                }
            }
            sendQueuedTxPacketsInWindow();
        }
    }

    @Override
    public void onServiceAvailable() {
        SHNLogger.d(TAG, "onServiceAvailable");
        state = SHNProtocolMoonshineStreamingState.AcquiringConfiguration;
    }

    @Override
    public void onServiceUnavailable() {
        SHNLogger.d(TAG, "onServiceUnavailable");
        setInitialState();
        if (shnProtocolMoonshineStreamingListener != null) {
            shnProtocolMoonshineStreamingListener.onProtocolUnavailable();
        }
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            SHNLogger.i(TAG, log);
        }
    }
}
