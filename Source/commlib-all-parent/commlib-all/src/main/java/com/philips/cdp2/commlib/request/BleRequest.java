package com.philips.cdp2.commlib.request;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommRequest;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;

import java.nio.ByteBuffer;

public class BleRequest extends Request {
    private final SHNDevice mShnDevice;
    private final String mPortName;
    private final int mProductId;
    private final LocalRequestType mRequestType;

    private final ResultListener<SHNDataRaw> mResultListener = new ResultListener<SHNDataRaw>() {
        @Override
        public void onActionCompleted(SHNDataRaw shnDataRaw, @NonNull SHNResult shnResult) {
            if (SHNResult.SHNOk.equals(shnResult)) {
                DiCommMessage message = new DiCommMessage(ByteBuffer.wrap(shnDataRaw.getRawData()));
                final DiCommResponse res = new DiCommResponse(message);

                mResponseHandler.onSuccess(res.getPropertiesAsString());
            } else {
                mResponseHandler.onError(Error.REQUESTFAILED, null);
            }
        }
    };

    public BleRequest(SHNDevice shnDevice, String portName, int productId, LocalRequestType requestType, ResponseHandler responseHandler) {
        super(null, responseHandler);

        mShnDevice = shnDevice;
        mPortName = portName;
        mProductId = productId;
        mRequestType = requestType;
    }

    @Override
    public Response execute() {
        switch (mRequestType) {
            case GET:
                final CapabilityDiComm capability = (CapabilityDiComm) mShnDevice.getCapabilityForType(SHNCapabilityType.DI_COMM);
                capability.addDataListener(mResultListener);

                DiCommMessage m = new DiCommRequest().getPropsRequestDataWithProduct(Integer.toString(mProductId), mPortName);
                capability.writeData(m.toData());
                break;
            case POST:
            case DELETE:
            case PUT:
            default:
                throw new IllegalStateException("Not implemented yet.");
        }
        return null;
    }
}
