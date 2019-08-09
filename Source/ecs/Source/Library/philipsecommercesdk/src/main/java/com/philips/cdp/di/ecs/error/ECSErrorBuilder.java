package com.philips.cdp.di.ecs.error;

import android.util.Pair;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;

public class ECSErrorBuilder {


    public Pair<GetDeliveryModes,ECSError>  getDeliveryModesECSErrorPair(JSONObject response){

        GetDeliveryModes getDeliveryModes=null;
        String detailError="";
        ECSError ecsError =null;

        try {
            if(null!=response && null!=response.toString()){
                detailError=response.toString();
            }
            getDeliveryModes  = new Gson().fromJson(response.toString(),
                    GetDeliveryModes.class);

            if(getDeliveryModes==null || getDeliveryModes.getDeliveryModes() == null || getDeliveryModes.getDeliveryModes().size() == 0){

                getDeliveryModes = null;
                Exception e = new Exception(ECSErrors.DeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorMessage());
                ecsError = new ECSError(e,ECSErrors.DeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorMessage(),ECSErrors.DeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorCode());
            }

        }catch (Exception e){
            ecsError = new ECSError(e, detailError,ECSErrors.DeliveryModeError.UNKNOWN_ERROR.getErrorCode());
        }finally {
            return new Pair<>(getDeliveryModes,ecsError);
        }

    }
}
