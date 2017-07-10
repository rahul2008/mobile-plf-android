package com.philips.platform.ths.intake;

import android.os.Bundle;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class THSSymptomsPresenter implements THSBasePresenter, THSVisitContextCallBack<THSVisitContext,THSSDKError> {
    THSBaseFragment pthBaseView;
    THSProviderInfo THSProviderInfo;
    THSVisitContext THSVisitContext;


    THSSymptomsPresenter(THSBaseFragment uiBaseView, THSProviderInfo THSProviderInfo){
        this.pthBaseView = uiBaseView;
        this.THSProviderInfo = THSProviderInfo;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            Bundle bundle = new Bundle();
            pthBaseView.addFragment(new THSVitalsFragment(), THSVitalsFragment.TAG,bundle);
        }
    }

    @Override
    public void onResponse(THSVisitContext THSVisitContext, THSSDKError THSSDKError) {
        this.THSVisitContext = THSVisitContext;
        final List<LegalText> legalTexts = THSVisitContext.getLegalTexts();
        for (LegalText legalText:legalTexts
             ) {
            legalText.setAccepted(true);
        }

        if(THSVisitContext !=null){
            ((THSSymptomsFragment)pthBaseView).addTopicsToView(THSVisitContext);
        }
        pthBaseView.hideProgressBar();
    }

    @Override
    public void onFailure(Throwable throwable) {
    }

    void getVisitContext()  {

        try {
            THSManager.getInstance().getVisitContext(pthBaseView.getFragmentActivity(), THSProviderInfo,this);
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
