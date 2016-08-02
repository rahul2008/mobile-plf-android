package com.philips.platform.appframework.inapppurchase;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * Created by 310173741 on 8/1/16.
 */
public class InAppPurchaseFragmentPresenter extends UIBasePresenter implements UICoCoUserRegImpl.SetStateCallBack {
    private AppFrameworkApplication appFrameworkApplication;

    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {
//        Logger.i("testing","IAP Presenter onLoad");
//        if(appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_WELCOME_REGISTRATION_STATE) {
//            setState(UIState.UI_WELCOME_REGISTRATION_STATE);
//            appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
//            uiCoCoInAppPurchase = (UICoCoInAppPurchase) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
//            uiCoCoInAppPurchase.registerForNextState(this);
//            uiCoCoInAppPurchase.setFragActivity((HomeActivity) context);
//            uiCoCoInAppPurchase.setFragmentContainer(R.id.frame_container);
//            appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_USER_REGISTRATION_STATE, context, this);
//        }
//        else{
//            setState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
//            ((HomeActivity)context).loadInAppPurchaseFragment();
//        }
    }

    @Override
    public void setNextState(Context context) {
//        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
//        appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_HOME_STATE, context, this);
    }
}
