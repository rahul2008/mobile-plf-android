package com.philips.cdp.digitalcare.contactus.fragments;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.request.RequestData;
import com.philips.cdp.digitalcare.request.ResponseCallback;


public class ContactUsPresenter implements ResponseCallback {

    private ContactUsContract contract;

    private CdlsResponseModel mCdlsParsedResponse = null;

    private boolean isFirstTimeCdlsCall = true;

    public ContactUsPresenter(ContactUsContract contract) {
        this.contract = contract;
    }

    private final CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            if (response !=null && response.getSuccess()) {
                mCdlsParsedResponse = response;
                updateUi();
            } else {
                if (isFirstTimeCdlsCall) {
                    isFirstTimeCdlsCall = false;
                    requestCdlsData();
                } else {
                    contract.fadeoutButtons();
                }
            }
        }
    };

    public void parseCdlsResponse(String response) {
        final CdlsResponseParser cdlsResponseParser = new CdlsResponseParser(
                mParsingCompletedCallback);
        cdlsResponseParser.parseCdlsResponse(response);
    }

    public void updateUi() {
        if (mCdlsParsedResponse.getSuccess()) {
            final CdlsPhoneModel phoneModel = mCdlsParsedResponse.getPhone();
            if (phoneModel != null) {
                if (phoneModel.getPhoneNumber() != null) {
                    contract.showCallPhilipsBtn();
                }
                contract.enableBottomText();
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(phoneModel.getOpeningHoursWeekdays())
                        .append(phoneModel.getOpeningHoursSaturday())
                        .append(phoneModel.getOpeningHoursSunday())
                        .append(phoneModel.getOptionalData1())
                        .append(phoneModel.getOptionalData2())
                        .append("\n" + phoneModel.getmPhoneTariff() + "\n");
                contract.enableBottomText();
                contract.setTextCallPhilipsBtn(mCdlsParsedResponse.getPhone().getPhoneNumber());
                contract.updateFirstRowSharePreference(stringBuilder,mCdlsParsedResponse.getPhone()
                        .getPhoneNumber());
            }
        } else if (isCdlsResponseModelNull()) {
            contract.fadeoutButtons();
        } else {
            contract.fadeoutButtons();
        }
    }

    private boolean isCdlsResponseModelNull() {
        return mCdlsParsedResponse.getError() != null;
    }

    protected void requestCdlsData() {
        contract.startProgressDialog();
        if (isCdlsUrlNull()) {
            String url = getCdlsUrl();
            RequestData requestData = new RequestData();
            requestData.setRequestUrl(url);
            requestData.setResponseCallback(this);
            requestData.execute();
        }
    }

    public boolean isCdlsUrlNull() {
        return getCdlsUrl() != null;
    }

    protected String getCdlsUrl() {
        return DigitalCareConfigManager.getInstance().getCdlsUrl();
    }

    @Override
    public void onResponseReceived(String response) {
        if (contract.isViewAdded()) {
            contract.closeProgressDialog();
            if (response != null && contract.isViewAdded()) {
                parseCdlsResponse(response);
            } else {
                if (isFirstTimeCdlsCall) {
                    isFirstTimeCdlsCall = false;
                    requestCdlsData();
                } else {
                    contract.fadeoutButtons();
                }
            }
        }
    }
}
