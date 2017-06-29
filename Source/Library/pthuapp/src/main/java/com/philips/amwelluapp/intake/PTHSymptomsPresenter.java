package com.philips.amwelluapp.intake;

import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class PTHSymptomsPresenter implements PTHBasePresenter, PTHVisitContextCallBack<PTHVisitContext,PTHSDKError>, PTHSDKValidatedCallback<Visit, SDKError>, PTHUpdateConsumerCallback <PTHConsumer, PTHSDKPasswordError> {
    PTHBaseView pthBaseView;
    PTHConsumer pthConsumer;
    PTHProviderInfo pthProviderInfo;


    PTHSymptomsPresenter(PTHBaseView uiBaseView, PTHConsumer pthConsumer, PTHProviderInfo pthProviderInfo){
        this.pthBaseView = uiBaseView;
        this.pthConsumer = pthConsumer;
        this.pthProviderInfo = pthProviderInfo;
    }

    @Override
    public void onEvent(int componentID) {

    }

    @Override
    public void onResponse(PTHVisitContext pthVisitContext, PTHSDKError pthsdkError) {
        Toast.makeText(pthBaseView.getFragmentActivity(),"OnSuccess - topics",Toast.LENGTH_SHORT).show();
        final List<LegalText> legalTexts = pthVisitContext.getLegalTexts();
        for (LegalText legalText:legalTexts
             ) {
            legalText.setAccepted(true);
        }

        if(pthVisitContext!=null){
            try {
                createVisit(pthVisitContext);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(Visit visit, SDKError sdkError) {

    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(pthBaseView.getFragmentActivity(),"OnFailure - topics",Toast.LENGTH_SHORT).show();
    }

    void getVisitContext() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        PTHManager.getInstance().updateConsumer(pthBaseView.getFragmentActivity(),pthConsumer,this);
    }

    void createVisit(PTHVisitContext visitContext) throws AWSDKInstantiationException {
        PTHManager.getInstance().createVisit(pthBaseView.getFragmentActivity(),visitContext,this);
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {

    }


    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onUpdateConsumerResponse(PTHConsumer pthConsumer, PTHSDKPasswordError sdkPasswordError) {
        try {
            PTHManager.getInstance().getVisitContext(pthBaseView.getFragmentActivity(),pthConsumer,pthProviderInfo,this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        } catch (AWSDKInitializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {

    }
}
