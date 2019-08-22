package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.error.ECSErrors.getErrorMessage;

public class SubmitOrderRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String>  {


    ECSCallback<OrderDetail,Exception> exceptionECSCallback;
    String cvv;

    public SubmitOrderRequest(String cvv,ECSCallback<OrderDetail, Exception> exceptionECSCallback) {
        this.cvv=cvv; // todo   reproduce returning user with saved payment method
        this.exceptionECSCallback = exceptionECSCallback;
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {
        Exception exception = null;
        OrderDetail orderDetail=null;
        try {
             orderDetail = new Gson().fromJson(response, OrderDetail.class);
        }catch(Exception e){
            exception=e;
        }
        if(null==exception && null!=orderDetail){
            exceptionECSCallback.onResponse(orderDetail);
        }else{
            exception = (null!=exception)? exception : new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR);
            exceptionECSCallback.onFailure(exception,""+response, 999);
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getPlaceOrderUrl();
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> cartId = new HashMap<>();
        cartId.put(ModelConstants.CART_ID,"current");
        return  cartId;
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        exceptionECSCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),4999);
    }


    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }



}
