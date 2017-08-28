package com.philips.platform.appframework.testmicroappfw.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateListener;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;


/**
 * Created by philips on 13/02/17.
 */

public class COCOListPresenter extends AbstractUIBasePresenter implements COCOListContract.UserActionsListener,UIStateListener{

    private static final  String TAG=COCOListPresenter.class.getSimpleName();

    public static final String USER_REGISTRATION_STANDARD = "Test User Registration Demo";
    public static final String PRODUCT_REGISTRATION="Test Product Registration Demo";
    public static final String IAP_DEMO_APP="Test In App Purchase Demo";
    public static final String USER_REGISTRATION_COPPA = "User Registration(Coppa)";
    public static final String DS_DEMO_APP="Test Data Services Demo";
    public static final String CC_DEMO_APP = "Test Consumer Care Demo";
    public static final String DLS_DEMO_APP = "Test DLS Demo";
    public static final String UAPP_FRAMEWORK_DEMO = "Test uApp Demo";
    public static final String DICOMM_APP="Test ConArtist Demo";
    public static final String BLUE_LIB_DEMO_APP="Test ShineLib Demo";
    public static final String DEMO_APP_INFRA="Test App Infra Demo";
    public static final String DEVICE_PAIRING_DEMO_APP="Test Device Pairing Demo";

    public static final String TEST_DS_EVENT="TestDataServicesEvent";
    public static final String TEST_IAP_EVENT="TestInAppPurhcaseEvent";
    public static final String TEST_PR_EVENT="TestProductRegistrationEvent";
    public static final String TEST_CC_EVENT="TestConsumerCareEvent";
    public static final String TEST_UAPP_EVENT="TestUAPPFrameworkEvent";
    public static final String TEST_UR_EVENT="TestUserRegistrationEvent";
    public static final String TEST_DICOMM_EVENT="TestDicommClientEvent";
    public static final String TEST_BLUE_LIB_DEMO_APP_EVENT="TestBlueLibEvent";
    public static final String TEST_DEVICE_PAIRING = "device_pairing";
    public static final String TEST_APP_INFRA = "AppInfra";
    public final COCOListContract.View cocoListContractView;
    public static final String TEST_DLS_APP="DLS";

    private BaseState baseState;

    private FragmentView view;

    private Context context;

    private TestConfigManager testConfigManager;

    public COCOListPresenter(FragmentView view, TestConfigManager testConfigManager, Context context,COCOListContract.View cocoListContractView) {
        super(view);
        this.view = view;
        this.cocoListContractView=cocoListContractView;
        this.context = context;
        this.testConfigManager=testConfigManager;
    }


    @Override
    public void loadCoCoList(Chapter chapter) {
        testConfigManager.getCoCoList(chapter, new TestConfigManager.TestConfigCallback() {
            @Override
            public void onChaptersLoaded(ArrayList<Chapter> chaptersList) {

            }

            @Override
            public void onCOCOLoaded(ArrayList<CommonComponent> commonComponentsList) {
                cocoListContractView.displayCoCoList(commonComponentsList);
            }

            @Override
            public void onCOCOLoadError() {

            }
        });
    }

    public void onEvent(String cocoName) {
        String eventState = getEventState(cocoName);
        RALog.d(TAG,"Event state:"+eventState);
        try {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.TEST_MICROAPP),eventState);

            if (baseState != null) {
                baseState.init(context.getApplicationContext());
                baseState.navigate(getFragmentLauncher());
            }
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(getClass() + "", e.getMessage());
        }
    }
    protected String getEventState(String cocoName) {
        switch (cocoName) {
            case IAP_DEMO_APP:
                return TEST_IAP_EVENT;
            case DS_DEMO_APP:
                return TEST_DS_EVENT;
            case PRODUCT_REGISTRATION:
                return TEST_PR_EVENT;
            case USER_REGISTRATION_STANDARD:
                return TEST_UR_EVENT;
            case CC_DEMO_APP:
                return TEST_CC_EVENT;
            case UAPP_FRAMEWORK_DEMO:
                return TEST_UAPP_EVENT;
            case DICOMM_APP:
                return TEST_DICOMM_EVENT;
            case BLUE_LIB_DEMO_APP:
                return TEST_BLUE_LIB_DEMO_APP_EVENT;
            case DEVICE_PAIRING_DEMO_APP:
                return TEST_DEVICE_PAIRING;
            case DEMO_APP_INFRA:
                return TEST_APP_INFRA;
            case DLS_DEMO_APP:
                return TEST_DLS_APP;
            default:
                return TEST_APP_INFRA;
        }
    }
    @Override
    public void onStateComplete(BaseState baseState) {

    }

    @Override
    public void onEvent(int componentID) {

    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) view.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(view.getFragmentActivity(), view.getContainerId(), view.getActionBarListener());
    }
}
