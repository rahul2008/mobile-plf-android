/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.error;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.request.CreateAddressRequest;
import com.philips.cdp.di.ecs.request.DeleteAddressRequest;
import com.philips.cdp.di.ecs.request.GetDeliveryModesRequest;
import com.philips.cdp.di.ecs.request.SetDeliveryAddressRequest;
import com.philips.cdp.di.ecs.request.SetDeliveryModesRequest;
import com.philips.cdp.di.ecs.request.UpdateAddressRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ECSNetworkError {


    private static final String LOGGING_TAG = ECSNetworkError.class.getSimpleName();

    public  static ECSErrorWrapper getErrorLocalizedErrorMessage(ECSErrorEnum ecsErrorEnum,Exception exception, String hysbrisResponse){
        String logMessage=ecsErrorEnum.toString();
        if(null!=exception){
            logMessage="\n\n"+exception.getLocalizedMessage();
        }
        if(null!=hysbrisResponse){
            logMessage="\n\n"+hysbrisResponse;
        }
        Log.e(LOGGING_TAG, logMessage);
        try {
            ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG+"getErrorLocalizedErrorMessage", logMessage);
        }catch(Exception e){

        }
        ECSError ecsError = new ECSError(ecsErrorEnum.getErrorCode(),ecsErrorEnum.toString());

        return  new  ECSErrorWrapper(new Exception(ecsErrorEnum.getLocalizedErrorString()), ecsError);
    }

    public static ECSErrorWrapper getErrorLocalizedErrorMessage(VolleyError volleyError,Object requestName) {

        String errorType = null;
        ECSErrorEnum ecsErrorEnum = ECSErrorEnum.ECSsomethingWentWrong;
        if (volleyError instanceof com.android.volley.ServerError || volleyError instanceof AuthFailureError) {
            ServerError serverError = getServerError(volleyError);
            if (serverError!=null && serverError.getErrors() != null && serverError.getErrors().size() != 0 && serverError.getErrors().get(0).getType() != null) {
                Log.e("ON_FAILURE_ERROR", serverError.getErrors().get(0).toString());
                errorType = "ECS"+serverError.getErrors().get(0).getType();
                if(requestName instanceof CreateAddressRequest || requestName instanceof DeleteAddressRequest || requestName instanceof GetDeliveryModesRequest ||
                        requestName instanceof SetDeliveryAddressRequest || requestName instanceof SetDeliveryModesRequest || requestName instanceof UpdateAddressRequest){
                    // for address related request error subject will be checked
                    if(null!=serverError.getErrors().get(0).getSubject()){
                        errorType="ECS"+serverError.getErrors().get(0).getSubject();
                    }
                }
                ecsErrorEnum = ECSErrorEnum.valueOf(errorType);
            }else   { // If it is AuthFailureError other than InvalidHybris Token
                ecsErrorEnum = getVolleyErrorType(volleyError);
            }
        } else {
            ecsErrorEnum = getVolleyErrorType(volleyError);
        }

        Exception exception = null;
        if(ecsErrorEnum==ECSErrorEnum.ECS_volley_error){ // if volley error
            errorType=ECSErrorEnum.ECS_volley_error.toString();
            exception = volleyError;
        }else{    // if Hybris error
            exception =   new Exception(ecsErrorEnum.getLocalizedErrorString());
        }
        ECSError ecsError = new ECSError( ecsErrorEnum.getErrorCode(),errorType);
        return new ECSErrorWrapper(exception,ecsError);
    }


    private static ECSErrorEnum getVolleyErrorType(final VolleyError error) {
        if(error.getMessage()!=null) {
            Log.e(LOGGING_TAG + " Volley Error: ", error.getMessage());
            try {
                ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG + " Volley Error: ", error.getMessage());
            } catch (Exception e) {

            }
        }
        return ECSErrorEnum.ECS_volley_error;
    }

    private static ServerError getServerError(VolleyError error) {
        try {
            if (error.networkResponse != null) {
                System.out.println("print base64 byte"+error.networkResponse.data);
                final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
                return parseServerError(encodedString);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static ServerError parseServerError(String encodedString) {
        final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
        final String errorString = new String(decode);
        JSONObject errorJsonObject =null;
        try {
            ECSConfig.INSTANCE.getEcsLogging().log(LoggingInterface.LogLevel.VERBOSE, LOGGING_TAG, errorString);
            errorJsonObject = new JSONObject(errorString);
        }catch (Exception e){

        }
        ServerError serverError=null;
        if(null!=errorJsonObject && errorJsonObject.has("error") && null!=errorJsonObject.optString("error")){ // if any auth error
            /*{
	    "error": "invalid_grant",
	    "error_description": "Invalid Janrain Token 47qyecdcks8uexus"
            }*/
            Error error = new Error();
            List<Error> errorList = new ArrayList<Error>();
            serverError = new ServerError();

            Gson gsonObj = new Gson();
            Map<String, String> inputMap = new HashMap<String, String>();
            inputMap.put("type", errorJsonObject.optString("error"));
            inputMap.put("message", errorJsonObject.optString("error_description",""));
            String jsonStr = gsonObj.toJson(inputMap);
            error = new Gson().fromJson(jsonStr, Error.class);
            errorList.add(error);
            serverError.setErrors(errorList);

        }else{
            serverError = new Gson().fromJson(errorString, ServerError.class);
        }
        return serverError;
    }

}
