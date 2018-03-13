/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.content.Context;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_PHARMACY;

public class THSSearchPharmacyPresenter implements THSBasePresenter, THSGetPharmaciesCallback {

    private Context context;
    private THSSearchFragmentViewInterface uiView;

    String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
    Pattern pattern = Pattern.compile(regex);

    public THSSearchPharmacyPresenter(Context context, THSSearchFragmentViewInterface uiView) {
        this.context = context;
        this.uiView = uiView;
    }

    @Override
    public void onEvent(int componentID) {

        if (componentID == THSSearchPharmacyFragment.SEARCH_EVENT_ID) {
            try {
                THSManager.getInstance().getPharmacies(context, THSManager.getInstance().getPTHConsumer(uiView.getFragmentActivity()), null, null, uiView.getZipCode(), this);
            } catch (AWSDKInstantiationException e) {

            }
        }
    }

    public boolean validateZip(String zipCode){
        return pattern.matcher(zipCode).matches();

    }

    @Override
    public void onValidationFailure(Map<String, String> map) {
        uiView.hideProgressBar();
    }

    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        uiView.hideProgressBar();
        if (null != sdkError) {
            uiView.showError(THSSDKErrorFactory.getErrorType(uiView.getFragmentActivity(), ANALYTICS_PHARMACY,sdkError));
        } else {
            uiView.setPharmacyList(pharmacies);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        uiView.hideProgressBar();
        uiView.showError(context.getResources().getString(R.string.ths_se_server_error_toast_message));

    }
}
