/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
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

public class BleRequest extends Request implements Runnable {
    private boolean mIsExecuting;
    private CapabilityDiComm mCapability;
    private final CountDownLatch mCountDownLatch;
    private final int mProductId;
    private final LocalRequestType mRequestType;
    private final Object mLock = new Object();
    private final String mPortName;

    private final DiCommByteStreamReader reader = new DiCommByteStreamReader(new DiCommByteStreamReader.DiCommMessageListener() {
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
                        reader.onBytes(shnDataRaw.getRawData());
                    }
                } else {
                    mResponseHandler.onError(Error.NOT_UNDERSTOOD, shnResult.name());
                    mCountDownLatch.countDown();
                }
            }
        }
    };

    public BleRequest(SHNDevice shnDevice, String portName, int productId, LocalRequestType requestType, Map<String, Object> dataMap, ResponseHandler responseHandler) {
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

            switch (mRequestType) {
                case GET:
                    mCapability.addDataListener(mResultListener);

                    DiCommMessage getPropsMessage = new DiCommRequest().getPropsRequestDataWithProduct(Integer.toString(mProductId), mPortName);
                    mCapability.writeData(getPropsMessage.toData());
                    break;
                case PUT:
                    mCapability.addDataListener(mResultListener);

                    DiCommMessage putPropsMessage = new DiCommRequest().putPropsRequestDataWithProduct(Integer.toString(mProductId), mPortName, mDataMap);
                    mCapability.writeData(putPropsMessage.toData());
                    break;
                case POST:
                case DELETE:
                default:
                    throw new IllegalStateException("Not implemented yet.");
            }
            return null;
        }
    }

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
