package com.philips.cdp.di.ecs.network;

import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.platform.appinfra.rest.TokenProviderInterface;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetProductJSONRequest implements APPInfraJSONRequest {

    private final int currentPage;
    private int pageSize = 20;
    private final ECSCallback<Products,Exception> ecsCallback;

    public GetProductJSONRequest(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
        this.currentPage = currentPage;
        this.ecsCallback = ecsCallback;
        if(pageSize !=0){
            this.pageSize = pageSize;
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getProductCatalogUrl(currentPage,pageSize);
    }

    @Override
    public JSONObject getJSONRequest() {
        return null;
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }

    @Override
    public Response.ErrorListener getJSONFailureResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(currentPage));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(pageSize));
        return query;
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return this;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        setServerError(error);
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) {
            Products resp = new Gson().fromJson(response.toString(),
                    Products.class);
            ecsCallback.onResponse(resp);
        }
    }

    @Override
    public Token getToken() {
        return null;
    }

    public void executeRequest(){
        new NetworkController(this).executeRequest();
    }

    private void setServerError(VolleyError error) {

        if (error.networkResponse != null) {
            final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
            final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
            final String errorString = new String(decode);

            System.out.println("Print volley error"+errorString);
        }
    }
}
