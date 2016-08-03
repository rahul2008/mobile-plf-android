package com.philips.cdp.prxclient.datamodels.summary;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;




public class SummaryModel extends ResponseData {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     *
     */
    public SummaryModel() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public SummaryModel(boolean success, Data data) {
        this.success = success;
        this.data = data;
    }

    /**
     *
     * @return
     * The success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public ResponseData parseJsonResponseData(JSONObject response) {
       SummaryModel mSummaryModel = new SummaryModel();
	   JSONObject summaryResponse = response;
	   if(summaryResponse != null)
	   {
        mSummaryModel = new Gson().fromJson(summaryResponse.toString(), SummaryModel.class);
       }else
	   {
		    mSummaryModel = new Gson().fromJson(summaryResponse, SummaryModel.class);
	   }
        return mSummaryModel;
    }
}