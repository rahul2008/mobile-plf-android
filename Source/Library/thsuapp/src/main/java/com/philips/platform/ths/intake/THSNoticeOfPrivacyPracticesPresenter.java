/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.faqs.THSFaqPresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSRestClient;
import com.philips.platform.ths.welcome.THSTermsAndConditionsFragment;

import java.net.URL;
import java.util.List;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.philips.platform.ths.utility.THSConstants.THS_FAQ_SERVICE_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITIONS;

public class THSNoticeOfPrivacyPracticesPresenter implements THSBasePresenter {
    private THSNoticeOfPrivacyPracticesFragment uiBaseView;
    protected THSVisitContext mTHSVisitContext;
    private StringBuilder mStringBuilder;
    private THSRestClient mTHSRestClient;

    public THSNoticeOfPrivacyPracticesPresenter(THSNoticeOfPrivacyPracticesFragment uiBaseView) {

        this.uiBaseView = uiBaseView;
        mTHSVisitContext = THSManager.getInstance().getPthVisitContext();
        mStringBuilder = new StringBuilder();
    }


    void showLegalTextForNOPP() {
        List<LegalText> legalTextList = mTHSVisitContext.getLegalTexts();
        for (LegalText legalText : legalTextList) {
            if (legalText.isRequired()) {
                mStringBuilder.append(legalText.getLegalText());
                mStringBuilder.append(System.getProperty("line.separator"));
                mStringBuilder.append(System.getProperty("line.separator"));
            }
        }
        final Spanned text = getSpannedText();
        if (null != text) {
            ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).legalTextsLabel.setText(text);
        }
    }

    @Override
    public void onEvent(int componentID) {

    }


    @SuppressWarnings("deprecation")
    private Spanned getSpannedText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(mStringBuilder.toString(), FROM_HTML_MODE_LEGACY); // for 24 api and more
        } else {
            return Html.fromHtml(mStringBuilder.toString());
        }
    }

    //callbacks
    /*@Override
    public void onNoticeOfPrivacyPracticesReceivedSuccess(String string, SDKError sDKError) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
        if (null != string) {
            mStringBuilder.append(System.getProperty("line.separator"));
            mStringBuilder.append(string);
            final Spanned text = getSpannedText();
            ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).legalTextsLabel.setText(text);
        }
    }

    @Override
    public void onNoticeOfPrivacyPracticesReceivedFailure(Throwable throwable) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
    }*/

    public void getTermsAndConditions(){
        THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_TERMS_AND_CONDITIONS, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                uiBaseView.showError("Service discovery failed - >" + s);
            }

            @Override
            public void onSuccess(URL url) {
                mTHSRestClient = new THSRestClient(THSNoticeOfPrivacyPracticesPresenter.this);

                mTHSRestClient.execute(url.toString());
              //  ((THSTermsAndConditionsFragment) uiBaseView).showTermsAndConditions(url.toString());
            }
        });
    }

    public void setText(String feed) {
        ((THSTermsAndConditionsFragment) uiBaseView).showTermsAndConditions(feed);
    }
}
