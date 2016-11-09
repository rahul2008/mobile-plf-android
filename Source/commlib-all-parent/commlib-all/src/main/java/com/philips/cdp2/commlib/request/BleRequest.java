/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.request;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.error.BleErrorMap;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import com.philips.pins.shinelib.dicommsupport.DiCommByteStreamReader;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommRequest;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;
import com.philips.pins.shinelib.dicommsupport.StatusCode;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidPayloadFormatException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidStatusCodeException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * The type BleRequest.
 * <p>
 * This type is used by {@link com.philips.cdp2.commlib.strategy.BleStrategy} to perform DiComm
 * requests to a BLE device. The request uses a {@link CountDownLatch} to block the thread on
 * which it is running until a call to either {@link ResponseHandler#onSuccess(String)} or
 * {@link ResponseHandler#onError(Error, String)} is made to ensure that all requests that are
 * dispatched in a queue are processed sequentially.
 */
public class BleRequest extends Request implements Runnable {
    private static final int MAX_PAYLOAD_LENGTH = (int) Math.pow(2, 16) - 1;

    private boolean mIsExecuting;
    private final CapabilityDiComm mCapability;
    private final CountDownLatch mCountDownLatch;
    private final int mProductId;
    private final LocalRequestType mRequestType;
    private final Object mLock = new Object();
    private final String mPortName;

    private final DiCommByteStreamReader mDiCommByteStreamReader = new DiCommByteStreamReader(new DiCommByteStreamReader.DiCommMessageListener() {
        @Override
        public void onMessage(DiCommMessage diCommMessage) {
            try {
                final DiCommResponse res = new DiCommResponse(diCommMessage);
                final StatusCode statusCode = res.getStatus();

                if (statusCode == StatusCode.NoError) {
                    mResponseHandler.onSuccess(res.getPropertiesAsString());
                } else {
                    final Error error = BleErrorMap.getErrorByStatusCode(statusCode);
                    mResponseHandler.onError(error, res.getPropertiesAsString());
                }
            } catch (IllegalArgumentException | InvalidMessageTerminationException | InvalidPayloadFormatException e) {
                mResponseHandler.onError(Error.PROTOCOL_VIOLATION, e.getMessage());
            } catch (InvalidStatusCodeException e) {
                mResponseHandler.onError(Error.UNKNOWN, e.getMessage());
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onError(String message) {
            mResponseHandler.onError(Error.NO_REQUEST_DATA, message);
            mCountDownLatch.countDown();
        }
    });

    private final ResultListener<SHNDataRaw> mResultListener = new ResultListener<SHNDataRaw>() {
        @Override
        public void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
            synchronized (mLock) {
                if (SHNResult.SHNOk.equals(shnResult)) {
                    if (mIsExecuting) {
                        mDiCommByteStreamReader.onBytes(shnDataRaw.getRawData());
                    }
                } else {
                    mResponseHandler.onError(Error.NOT_UNDERSTOOD, shnResult.name());
                    mCountDownLatch.countDown();
                }
            }
        }
    };

    /**
     * Instantiates a new BleRequest.
     *
     * @param shnDevice       the shn device
     * @param portName        the port name
     * @param productId       the product id
     * @param requestType     the request type
     * @param dataMap         the optional data map
     * @param responseHandler the response handler
     */
    public BleRequest(@NonNull SHNDevice shnDevice,
                      @NonNull String portName,
                      @NonNull int productId,
                      @NonNull LocalRequestType requestType,
                      Map<String, Object> dataMap,
                      @NonNull ResponseHandler responseHandler) {
        super(dataMap, responseHandler);

        mCapability = (CapabilityDiComm) shnDevice.getCapabilityForType(SHNCapabilityType.DI_COMM);
        mPortName = portName;
        mProductId = productId;
        mRequestType = requestType;
        mCountDownLatch = new CountDownLatch(1);
    }

    @Override
    public Response execute() {
        synchronized (mLock) {
            mIsExecuting = true;

            if (mPortName.isEmpty()) {
                mResponseHandler.onError(Error.NO_SUCH_PORT, "Port name is empty.");
                mCountDownLatch.countDown();

                return null;
            }
            mCapability.addDataListener(mResultListener);

            switch (mRequestType) {
                case GET:
                    DiCommMessage getPropsMessage = new DiCommRequest().getPropsRequestDataWithProduct(Integer.toString(mProductId), mPortName);
                    mCapability.writeData(getPropsMessage.toData());
                    break;
                case PUT:
                    if (mDataMap == null) {
                        mResponseHandler.onError(Error.INVALID_PARAMETER, "No request data supplied.");
                        mCountDownLatch.countDown();

                        return null;
                    }

                    if (mDataMap.isEmpty()) {
                        mResponseHandler.onError(Error.NO_REQUEST_DATA, "Request data is empty.");
                        mCountDownLatch.countDown();

                        return null;
                    }
                    final DiCommMessage putPropsMessage = new DiCommRequest().putPropsRequestDataWithProduct(Integer.toString(mProductId), mPortName, mDataMap);

                    if (putPropsMessage.getPayload().length > MAX_PAYLOAD_LENGTH) {
                        mResponseHandler.onError(Error.INVALID_PARAMETER, "Payload too big.");
                        mCountDownLatch.countDown();

                        return null;
                    }
                    mCapability.writeData(putPropsMessage.toData());
                    break;
                case POST:
                case DELETE:
                default:
                    throw new UnsupportedOperationException("Not implemented yet.");
            }
            return null;
        }
    }

    /**
     * Cancel.
     * <p>
     * This cancels the current request and notifies the {@link ResponseHandler} for this request.
     *
     * @param reason the reason
     */
    public void cancel(String reason) {
        synchronized (mLock) {
            mIsExecuting = false;
            mCapability.removeDataListener(mResultListener);
            mResponseHandler.onError(Error.REQUEST_FAILED, reason);
            mCountDownLatch.countDown();
        }
    }

    @Override
    public void run() {
        execute();

        try {
            mCountDownLatch.await();
        } catch (InterruptedException ignored) {
        }
    }
}
