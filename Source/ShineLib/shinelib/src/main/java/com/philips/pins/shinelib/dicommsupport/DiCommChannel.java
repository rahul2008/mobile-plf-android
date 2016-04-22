package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class DiCommChannel implements SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener {

    public static final int DEFAULT_TIME_OUT = 4000;
    private static final String TAG = "DiCommChannel";

    private final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming;
    private final int timeOut;
    private boolean isAvailable;

    private Set<DiCommPort> diCommPorts = new HashSet<>();
    private List<SHNMapResultListener<String, Object>> pendingRequests = new ArrayList<>();
    private byte[] receiveBuffer;

    public DiCommChannel(SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming, int timeOut) {
        this.shnProtocolMoonshineStreaming = shnProtocolMoonshineStreaming;
        shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(this);
        this.timeOut = timeOut;
    }

    // implements SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingLister
    @Override
    public void onDataReceived(byte[] data) {
        if (receiveBuffer == null) {
            receiveBuffer = data;
        } else {
            byte[] newReceiveBuffer = new byte[receiveBuffer.length + data.length];
            ByteBuffer byteBuffer = ByteBuffer.wrap(newReceiveBuffer);

            byteBuffer.put(receiveBuffer);
            byteBuffer.put(data);
            receiveBuffer = newReceiveBuffer;
        }

        try {
            DiCommMessage diCommMessage = new DiCommMessage(receiveBuffer);
            parseResponse(diCommMessage);
        } catch (InvalidParameterException ignored) {
            // can not detect a message
        }
    }

    private void parseResponse(DiCommMessage diCommMessage) {
        try {
            DiCommResponse diCommResponse = getDiCommResponse(diCommMessage);
            pendingRequests.get(0).onActionCompleted(diCommResponse.getProperties(), SHNResult.SHNOk);
        } catch (InvalidParameterException ex) {

        }
    }

    @Override
    public void onProtocolAvailable() {
        setAvailable(true);
    }

    @Override
    public void onProtocolUnavailable() {
        setAvailable(false);
    }

    private void setAvailable(boolean isAvailable) {
        if (this.isAvailable != isAvailable) {
            this.isAvailable = isAvailable;
            for (DiCommPort port : diCommPorts) {
                port.onChannelAvailabilityChanged(isAvailable);
            }
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void addPort(@NonNull DiCommPort diCommPort) {
        this.diCommPorts.add(diCommPort);

        diCommPort.onChannelAvailabilityChanged(isAvailable);
    }

    public void sendProperties(Map<String, Object> properties, String port, SHNMapResultListener<String, Object> resultListener) {
        try {
            DiCommRequest diCommRequest = getDiCommRequest();
            DiCommMessage diCommMessage = diCommRequest.putPropsRequestDataWithProduct("0", port, properties);

            if (!isAvailable) {
                resultListener.onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
            } else {
                pendingRequests.add(resultListener);
                if (pendingRequests.size() == 1) {
                    shnProtocolMoonshineStreaming.sendData(diCommMessage.toData());
                }
            }
        } catch (NullPointerException ex) {
            resultListener.onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
        }
    }

    @NonNull
    protected DiCommRequest getDiCommRequest() {
        return new DiCommRequest();
    }

    @NonNull
    protected DiCommResponse getDiCommResponse(DiCommMessage diCommMessage) {
        return new DiCommResponse(diCommMessage);
    }
}
