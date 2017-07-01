package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class PTHSymptomsPresenter implements PTHBasePresenter, PTHVisitContextCallBack<PTHVisitContext,PTHSDKError>{
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
        final List<LegalText> legalTexts = pthVisitContext.getLegalTexts();
        for (LegalText legalText:legalTexts
             ) {
            legalText.setAccepted(true);
        }

        if(pthVisitContext!=null){
            ((PTHSymptomsFragment)pthBaseView).addTopicsToView(pthVisitContext);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
    }

    void getVisitContext()  {
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
}
