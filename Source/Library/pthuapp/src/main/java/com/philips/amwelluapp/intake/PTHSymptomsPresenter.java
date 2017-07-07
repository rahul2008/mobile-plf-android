package com.philips.amwelluapp.intake;

import android.os.Bundle;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHConstants;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class PTHSymptomsPresenter implements PTHBasePresenter, PTHVisitContextCallBack<PTHVisitContext,PTHSDKError>{
    PTHBaseFragment pthBaseView;
    PTHProviderInfo pthProviderInfo;
    PTHVisitContext pthVisitContext;


    PTHSymptomsPresenter(PTHBaseFragment uiBaseView, PTHProviderInfo pthProviderInfo){
        this.pthBaseView = uiBaseView;
        this.pthProviderInfo = pthProviderInfo;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            Bundle bundle = new Bundle();
            pthBaseView.addFragment(new THSVitalsFragment(), THSVitalsFragment.TAG,bundle);
        }
    }

    @Override
    public void onResponse(PTHVisitContext pthVisitContext, PTHSDKError pthsdkError) {
        this.pthVisitContext = pthVisitContext;
        final List<LegalText> legalTexts = pthVisitContext.getLegalTexts();
        for (LegalText legalText:legalTexts
             ) {
            legalText.setAccepted(true);
        }

        if(pthVisitContext!=null){
            ((PTHSymptomsFragment)pthBaseView).addTopicsToView(pthVisitContext);
        }
        pthBaseView.hideProgressBar();
    }

    @Override
    public void onFailure(Throwable throwable) {
    }

    void getVisitContext()  {

        try {
            PTHManager.getInstance().getVisitContext(pthBaseView.getFragmentActivity(),pthProviderInfo,this);
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
