package com.philips.multiproduct.prx;

/**
 * This is the PrxSummaryDataListener class to receive the data from the Multiple Prx data requests.
 * This callback class  is used to manage to multiple data requests in the many data requests scenario.
 * <p/>
 * <p/>
 * Created by naveen@philips.com on 01-Feb-16.
 */
public interface PrxSummaryDataListener {

    /**
     * onprxData Successfull scenario's - it gives the loaded data.
     *
     * @param productData
     */
    abstract void onSuccess(ProductData productData);

    /**
     * This method is responsible to get the error messages which has been intimated by volley library.
     *
     * @param errorMessage
     */
    void onFail(String errorMessage);

}
