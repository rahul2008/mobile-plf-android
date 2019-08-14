package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class MakePaymentRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private  ECSCallback<MakePaymentData,Exception> ecsCallback;
    private OrderDetail orderDetail;
    Addresses ecsBillingAddressRequest;

    public MakePaymentRequest(OrderDetail orderDetail, Addresses ecsBillingAddressRequest, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.orderDetail = orderDetail;
        this.ecsBillingAddressRequest = ecsBillingAddressRequest;
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {

        MakePaymentData makePaymentData=null;
        Exception exception = null;
        try {
            makePaymentData = new Gson().fromJson(response, MakePaymentData.class);
        }catch(Exception e){
            exception=e;
        }

        if(null==exception && null!=makePaymentData){
            ecsCallback.onResponse(makePaymentData);
        }else{
            exception = (null!=exception)? exception : new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR);
            ecsCallback.onFailure(exception,""+response,9000);
        }

    }

    @Override
    public int getMethod() {
         return Request.Method.POST;
    }

    @Override
    public String getURL() {

        return new ECSURLBuilder().getMakePaymentUrl(orderDetail.getCode()); // orderID
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),4999);
    }

    @Override
    public Map<String, String> getParams() {
        return ECSRequestUtility.getAddressParams(ecsBillingAddressRequest);

    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }
}
