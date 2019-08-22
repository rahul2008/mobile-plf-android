package com.philips.cdp.di.ecs.error;

import android.util.Pair;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;

import org.json.JSONObject;

public class ECSErrorBuilder {


    public Pair<GetDeliveryModes, ECSError> getDeliveryModesECSErrorPair(JSONObject response) {

        GetDeliveryModes getDeliveryModes = null;
        String detailError = "";
        ECSError ecsError = null;

        try {
            if (null != response && null != response.toString()) {
                detailError = response.toString();
            }
            getDeliveryModes = new Gson().fromJson(response.toString(),
                    GetDeliveryModes.class);

            if (getDeliveryModes == null || getDeliveryModes.getDeliveryModes() == null || getDeliveryModes.getDeliveryModes().size() == 0) {

                getDeliveryModes = null;
                Exception e = new Exception(ECSErrorConstant.GetDeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorMessage());
                ecsError = new ECSError(e, ECSErrorConstant.GetDeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorMessage(), ECSErrorConstant.GetDeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorCode());
            }

        } catch (Exception e) {
            ecsError = new ECSError(e, detailError, ECSErrorConstant.GetDeliveryModeError.UNKNOWN_ERROR.getErrorCode());
        } finally {
            return new Pair<>(getDeliveryModes, ecsError);
        }

    }

    public Pair<Boolean, ECSError> getEmptyResponseErrorPair(String response) {

        boolean isValidResponse = false;
        String detailError = ECSErrorConstant.GetDeliveryModeError.UNKNOWN_ERROR.getErrorMessage(); //TODO ,Message and error code for diffrent calls .
        ECSError ecsError = null;
        Exception e = null;

        if (response != null) {

            if (response.trim().isEmpty()) {
                isValidResponse = true;
            } else {
                detailError = response;
                e = new Exception(detailError);
            }
        } else {
            e = new Exception(detailError);
        }
        if(e!=null) ecsError = new ECSError(e, detailError, ECSErrorConstant.GetDeliveryModeError.UNKNOWN_ERROR.getErrorCode());

        return new Pair<>(isValidResponse, ecsError);
    }
}
