/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidPayloadFormatException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidStatusCodeException;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiCommChannel implements SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener {

    private static final String TAG = "DiCommChannel";
    public static final String PRODUCT = "0";

    private final SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming;
    private boolean isAvailable;

    private Set<DiCommPort> diCommPorts = new HashSet<>();
    private List<RequestInfo> pendingRequests = new ArrayList<>();

    @NonNull
    private DiCommByteStreamReader mDiCommByteStreamReader;

    private DiCommByteStreamReader.DiCommMessageListener mDiCommMessageListener = new DiCommByteStreamReader.DiCommMessageListener() {
        @Override
        public void onMessage(DiCommMessage message) {
            parseResponse(message);
            executeNextRequest();
        }

        @Override
        public void onError(String errorMessage) {
            SHNLogger.e(TAG, errorMessage);
        }
    };

    private final Timer requestTimer;

    private void requestTimedOut() {
        SHNLogger.d(TAG, "requestTimedOut for " + pendingRequests.get(0).getRequestMessage().getMessageType());

        shnProtocolMoonshineStreaming.transitionToError(SHNResult.SHNErrorTimeout);
    }

    public DiCommChannel(SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming, int timeOut) {
        this.shnProtocolMoonshineStreaming = shnProtocolMoonshineStreaming;
        shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(this);
        requestTimer = Timer.createTimer(new Runnable() {
            @Override
            public void run() {
                requestTimedOut();
            }
        }, timeOut);

        mDiCommByteStreamReader = new DiCommByteStreamReader(mDiCommMessageListener);
    }

    // implements SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingLister
    @Override
    public void onDataReceived(byte[] data) {
        mDiCommByteStreamReader.onBytes(data);
    }

    private void executeNextRequest() {
        if (pendingRequests.size() > 0) {
            DiCommMessage diCommMessage = pendingRequests.get(0).getRequestMessage();
            shnProtocolMoonshineStreaming.sendData(diCommMessage.toData());
            requestTimer.restart();
        } else {
            requestTimer.stop();
        }
    }

    private void parseResponse(DiCommMessage diCommMessage) {
        try {
            DiCommResponse diCommResponse = getDiCommResponse(diCommMessage);
            SHNLogger.d(TAG, "Response status: " + diCommResponse.getStatus() + " properties: " + diCommResponse.getProperties());
            reportToListener(diCommResponse.getProperties(), convertToSHNResult(diCommResponse.getStatus()));
        } catch (IllegalArgumentException | InvalidStatusCodeException | InvalidPayloadFormatException | InvalidMessageTerminationException ex) {
            SHNLogger.e(TAG, ex.getMessage());
            reportToListener(null, SHNResult.SHNErrorInvalidParameter);
        }
    }

    private void reportToListener(Map<String, Object> properties, SHNResult result) {
        if (!pendingRequests.isEmpty()) {
            RequestInfo requestInfo = pendingRequests.get(0);
            requestInfo.getResultListener().onActionCompleted(properties, result);
            pendingRequests.remove(0);
        } else {
            SHNLogger.e(TAG, "Unexpected message with properties " + properties);
        }
    }

    private SHNResult convertToSHNResult(StatusCode status) {
        switch (status) {
            case NoError:
                return SHNResult.SHNOk;
            case NotUnderstood:
            case OutOfMemory:
            case NotSubscribed:
                return SHNResult.SHNErrorOperationFailed;
            case NotImplemented:
            case NoSuchPort:
            case NoSuchProperty:
            case NoSuchOperation:
            case VersionNotSupported:
            case NoSuchProduct:
            case NoSuchMethod:
            case PropertyAlreadyExists:
                return SHNResult.SHNErrorUnsupportedOperation;
            case WrongParameters:
            case InvalidParameter:
                return SHNResult.SHNErrorInvalidParameter;
            case ProtocolViolation:
                return SHNResult.SHNErrorInvalidState;
            default:
                return SHNResult.SHNErrorUnknown;
        }
    }

    @Override
    public void onProtocolAvailable() {
        setAvailable(true);
        shnProtocolMoonshineStreaming.transitionToReady();
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

            if (!isAvailable) {
                for (RequestInfo requestInfo : pendingRequests) {
                    requestInfo.getResultListener().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
                }
                pendingRequests.clear();
                requestTimer.stop();

                mDiCommByteStreamReader = new DiCommByteStreamReader(mDiCommMessageListener);
            }
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void addPort(@NonNull DiCommPort diCommPort) {
        this.diCommPorts.add(diCommPort);

        diCommPort.setDiCommChannel(this);

        diCommPort.onChannelAvailabilityChanged(isAvailable);
    }

    public void reloadProperties(String port, SHNMapResultListener<String, Object> resultListener) {
        SHNLogger.d(TAG, "reloadProperties");
        DiCommRequest diCommRequest = getDiCommRequest();
        DiCommMessage diCommMessage = diCommRequest.getPropsRequestDataWithProduct(PRODUCT, port);

        performRequest(diCommMessage, resultListener);
    }

    public void sendProperties(Map<String, Object> properties, String port, SHNMapResultListener<String, Object> resultListener) {
        SHNLogger.d(TAG, "sendProperties");

        DiCommRequest diCommRequest = getDiCommRequest();
        DiCommMessage diCommMessage = diCommRequest.putPropsRequestDataWithProduct(PRODUCT, port, properties);

        if (diCommMessage != null) {
            performRequest(diCommMessage, resultListener);
        } else {
            resultListener.onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
        }
    }

    private void performRequest(DiCommMessage diCommMessage, SHNMapResultListener<String, Object> resultListener) {
        if (!isAvailable) {
            resultListener.onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
        } else {
            pendingRequests.add(new RequestInfo(diCommMessage, resultListener));
            if (pendingRequests.size() == 1) {
                executeNextRequest();
            }
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

    private static class RequestInfo {
        private final DiCommMessage requestMessage;
        private final SHNMapResultListener<String, Object> resultListener;

        public RequestInfo(@NonNull DiCommMessage requestMessage, @NonNull SHNMapResultListener<String, Object> resultListener) {
            this.requestMessage = requestMessage;
            this.resultListener = resultListener;
        }

        public DiCommMessage getRequestMessage() {
            return requestMessage;
        }

        public SHNMapResultListener<String, Object> getResultListener() {
            return resultListener;
        }
    }
}
