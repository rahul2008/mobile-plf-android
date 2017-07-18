package com.philips.platform.ths.intake;

import android.os.Bundle;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
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
            pthBaseView.addFragment(new THSVitalsFragment(), THSVitalsFragment.TAG,null);
        }
    }

    @Override
    public void onResponse(THSVisitContext THSVisitContext, THSSDKError THSSDKError) {
        updateSymptoms(THSVisitContext);
    }

    private void updateSymptoms(THSVisitContext THSVisitContext) {
        this.THSVisitContext = THSVisitContext;

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

    public void getfirstavailableprovider(THSOnDemandSpeciality onDemandSpecialties) throws AWSDKInstantiationException {
        THSManager.getInstance().getVisitContextWithOnDemandSpeciality(pthBaseView.getContext(),onDemandSpecialties, new THSVisitContextCallBack<THSVisitContext, THSSDKError>() {
            @Override
            public void onResponse(THSVisitContext pthVisitContext, THSSDKError thssdkError) {
               updateSymptoms(pthVisitContext);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthBaseView.hideProgressBar();
            }
        });
    }
}
