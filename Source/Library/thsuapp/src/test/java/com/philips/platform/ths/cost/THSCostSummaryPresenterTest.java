/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.widget.RelativeLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.entity.visit.VisitCost;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.insurance.THSSubscription;
import com.philips.platform.ths.payment.THSPaymentMethod;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSCostSummaryPresenterTest {

    THSCostSummaryPresenter mTHSCostSummaryPresenter;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSCostSummaryFragment thsCostSummaryFragmentMock;

    @Mock
    THSVisit thsVisitMock;

    @Mock
    EditText editTextMock;

    @Mock
    Visit visitMock;

    @Mock
    Editable editableMock;

    @Mock
    RelativeLayout relativeLayoutMock;

    @Mock
    FragmentActivity fragmentActivityMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    VisitCost visitCostMock;

    @Mock
    Label labelMock;

    @Mock
    User userMock;

    @Mock
    Button buttonMock;

    @Mock
    Subscription subscriptionMock;

    @Mock
    HealthPlan healthPlanMock;

    @Mock
    THSSubscription thsSubscriptionMock;

    @Mock
    Relationship relationshipMock;

    @Mock
    Throwable throwableMock;

    @Mock
    THSPaymentMethod thsPaymentMethodMock;

    @Mock
    PaymentMethod paymentMethodMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);

        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        when(thsCostSummaryFragmentMock.getThsVisit()).thenReturn(thsVisitMock);
        when(thsVisitMock.getVisit()).thenReturn(visitMock);
        when(visitMock.getVisitCost()).thenReturn(visitCostMock);
        when(visitCostMock.getExpectedConsumerCopayCost()).thenReturn(12.0);
        when(thsSubscriptionMock.getSubscription()).thenReturn(subscriptionMock);
        when(healthPlanMock.getName()).thenReturn("sssss");

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(thsCostSummaryFragmentMock.getThsVisit()).thenReturn(thsVisitMock);
        when(thsVisitMock.getVisit()).thenReturn(visitMock);
        mTHSCostSummaryPresenter = new THSCostSummaryPresenter(thsCostSummaryFragmentMock);
        thsCostSummaryFragmentMock.costBigLabel = labelMock;
        thsCostSummaryFragmentMock.mInitialVisitCostLabel = labelMock;
        thsCostSummaryFragmentMock.mCostSummaryContinueButton = buttonMock;
        thsCostSummaryFragmentMock.mCouponCodeButton = buttonMock;
        thsCostSummaryFragmentMock.costSmallLabel = labelMock;
        thsCostSummaryFragmentMock.mNoInsuranceDetailRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mInsuranceDetailRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mInsuranceName = labelMock;
        thsCostSummaryFragmentMock.mInsuranceMemberId = labelMock;
        thsCostSummaryFragmentMock.mInsuranceSubscriptionType = labelMock;
        thsCostSummaryFragmentMock.mPaymentMethodDetailRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mPaymentNotRequired = labelMock;
        thsCostSummaryFragmentMock.mNoPaymentMethodDetailRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mCostSummaryContinueButtonRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mAddPaymentMethodButtonRelativeLayout = relativeLayoutMock;
        thsCostSummaryFragmentMock.mCardType = labelMock;
        thsCostSummaryFragmentMock.mMaskedCardNumber = labelMock;
        thsCostSummaryFragmentMock.mCardExpirationDate = labelMock;
        thsCostSummaryFragmentMock.mCouponCodeEdittext = editTextMock;
    }

    @Test
    public void onEvent_Continue() throws Exception {
        mTHSCostSummaryPresenter.onEvent(R.id.ths_cost_summary_continue_button);
        verify(thsCostSummaryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }


    @Test
    public void onEvent_payment_detail() throws Exception {
        mTHSCostSummaryPresenter.onEvent(R.id.ths_cost_summary_payment_detail_framelayout);
        verify(thsCostSummaryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onEvent_insurance_detail() throws Exception {
        thsCostSummaryFragmentMock.mCouponCodeEdittext = editTextMock;
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editTextMock.toString()).thenReturn("aaa");
        mTHSCostSummaryPresenter.onEvent(R.id.ths_cost_summary_insurance_detail_framelayout);
        verify(thsCostSummaryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test(expected = NullPointerException.class)
    public void onEvent_promotion() throws Exception {
        mTHSCostSummaryPresenter.onEvent(R.id.ths_cost_summary_promotion_code_apply_button);
        verifyNoMoreInteractions(thsCostSummaryFragmentMock);
    }


    @Test
    public void fetchExistingSubscription() throws Exception {
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        mTHSCostSummaryPresenter.fetchExistingSubscription();
        verify(consumerManagerMock).getInsuranceSubscription(any(Consumer.class), any(SDKCallback.class));
        verifyNoMoreInteractions(consumerManagerMock);
    }

    @Test
    public void onCreateVisitResponse_when_sdk_error_reason_null() throws Exception {
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        mTHSCostSummaryPresenter.onCreateVisitResponse(thsVisitMock,thssdkErrorMock);
        verify(thsCostSummaryFragmentMock).showError(anyString(),anyBoolean());
    }

    @Test(expected = NullPointerException.class)
    public void onCreateVisitResponse_when_sdk_error_reason_Not_null() throws Exception {

        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.PROVIDER_OFFLINE);

        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        mTHSCostSummaryPresenter.onCreateVisitResponse(thsVisitMock,thssdkErrorMock);
        verify(thsCostSummaryFragmentMock).showError(anyString(),anyBoolean());
    }

    @Test
    public void onCreateVisitResponse_when_sdk_error_null() throws Exception {
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        when(visitCostMock.getExpectedConsumerCopayCost()).thenReturn(12.5);
        mTHSCostSummaryPresenter.onCreateVisitResponse(thsVisitMock,null);
        final boolean enabled = thsCostSummaryFragmentMock.mCostSummaryContinueButton.isEnabled();
//        assert enabled == true;
    }

    @Test
    public void onCreateVisitResponse_when_subscription_not_null() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        when(consumerMock.getSubscription()).thenReturn(subscriptionMock);
        when(subscriptionMock.getHealthPlan()).thenReturn(healthPlanMock);
        when(thsVisitMock.getCouponCodeApplied()).thenReturn("122");
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        when(visitCostMock.getExpectedConsumerCopayCost()).thenReturn(12.5);
        mTHSCostSummaryPresenter.onCreateVisitResponse(thsVisitMock,null);
        final boolean enabled = thsCostSummaryFragmentMock.mCostSummaryContinueButton.isEnabled();
//        assert enabled == true;
    }


    @Test
    public void onCreateVisitResponse_when_visit_cost_free() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        when(visitCostMock.isFree()).thenReturn(true);
        mTHSCostSummaryPresenter.onCreateVisitResponse(thsVisitMock,null);
        final boolean enabled = thsCostSummaryFragmentMock.mCostSummaryContinueButton.isEnabled();
//        assert enabled == true;
    }

    @Test(expected = NullPointerException.class)
    public void applyCouponCode(){
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        mTHSCostSummaryPresenter.applyCouponCode("123");
    }

    @Test(expected = NullPointerException.class)
    public void onCreateVisitFailure() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        mTHSCostSummaryPresenter.onCreateVisitFailure(throwableMock);
        assertNotNull(thsCostSummaryFragment.alertDialogFragmentCreateVisit);
    }

    @Test(expected = NullPointerException.class)
    public void onCreateVisitValidationFailure() throws Exception {
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        Map<String, ValidationReason> map= new HashMap();
        map.put("spo",ValidationReason.FIELD_ATTACHMENT_TOO_BIG);

        mTHSCostSummaryPresenter.onCreateVisitValidationFailure(map);
        verify(thsCostSummaryFragmentMock).hideProgressBar();
    }

    @Test
    public void onGetInsuranceResponse() throws Exception {
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        mTHSCostSummaryPresenter.onGetInsuranceResponse(thsSubscriptionMock,thssdkErrorMock);
        verify(thsCostSummaryFragmentMock).showError(anyString());
    }

    @Test
    public void onGetInsuranceResponse_sdkerror_null() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thsSubscriptionMock.getSubscription()).thenReturn(subscriptionMock);
        when(subscriptionMock.getHealthPlan()).thenReturn(healthPlanMock);
        when(healthPlanMock.getName()).thenReturn("111");
        when(subscriptionMock.getRelationship()).thenReturn(relationshipMock);
        when(relationshipMock.getName()).thenReturn("baby");
        mTHSCostSummaryPresenter.onGetInsuranceResponse(thsSubscriptionMock,null);
        assertNotNull(thsCostSummaryFragment.mInsuranceSubscriptionType);
    }

    @Test
    public void onGetInsuranceResponse_tHSSubscription_null() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        /*when(thsSubscriptionMock.getSubscription()).thenReturn(subscriptionMock);
        when(subscriptionMock.getHealthPlan()).thenReturn(healthPlanMock);
        when(healthPlanMock.getName()).thenReturn("111");
        when(subscriptionMock.getRelationship()).thenReturn(relationshipMock);
        when(relationshipMock.getName()).thenReturn("baby");*/
        mTHSCostSummaryPresenter.onGetInsuranceResponse(null,null);
        assertNotNull(thsCostSummaryFragment.mInsuranceSubscriptionType);
    }

    @Test(expected = NullPointerException.class)
    public void onGetInsuranceFailure() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        mTHSCostSummaryPresenter.onGetInsuranceFailure(throwableMock);
        assertNotNull(thsCostSummaryFragment.mInsuranceSubscriptionType);
    }

    @Test
    public void onGetPaymentMethodResponse() throws Exception {
        when(thsCostSummaryFragmentMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(thsCostSummaryFragmentMock.isFragmentAttached()).thenReturn(true);
        mTHSCostSummaryPresenter.onGetPaymentMethodResponse(thsPaymentMethodMock,thssdkErrorMock);
        assertNotNull( thsCostSummaryFragmentMock.mCostSummaryContinueButtonRelativeLayout);
    }

    @Test
    public void onGetPaymentMethodResponse_payment_method_not_null() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        when(thsPaymentMethodMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        mTHSCostSummaryPresenter.onGetPaymentMethodResponse(thsPaymentMethodMock,thssdkErrorMock);
        assertNotNull( thsCostSummaryFragmentMock.mCostSummaryContinueButtonRelativeLayout);
    }

    @Test
    public void onGetPaymentMethodResponse_payment_method_not_not_expired() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        when(thsPaymentMethodMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(paymentMethodMock.isExpired()).thenReturn(true);

        mTHSCostSummaryPresenter.onGetPaymentMethodResponse(thsPaymentMethodMock,thssdkErrorMock);
        assertNotNull( thsCostSummaryFragmentMock.mCostSummaryContinueButtonRelativeLayout);
    }

    @Test(expected = NullPointerException.class)
    public void onGetPaymentFailure() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        mTHSCostSummaryPresenter.onGetPaymentFailure(throwableMock);
    }

    @Test(expected = NullPointerException.class)
    public void onApplyCouponResponse() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;

        mTHSCostSummaryPresenter.onApplyCouponResponse(null,thssdkErrorMock);
    }

    @Test
    public void onApplyCouponResponse_thsError_isNull() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);
        thsCostSummaryFragment.mCouponCodeEdittext.setText("222");
        thsCostSummaryFragment.thsVisit = thsVisitMock;
        when(thsVisitMock.getVisit()).thenReturn(visitMock);
        mTHSCostSummaryPresenter.mTHSCostSummaryFragment = thsCostSummaryFragment;
        when(thssdkErrorMock.getSdkError()).thenReturn(null);
        mTHSCostSummaryPresenter.onApplyCouponResponse(null,thssdkErrorMock);
    }

    @Test
    public void onApplyCouponFailure() throws Exception {
        THSCostSummaryFragment thsCostSummaryFragment = new THSCostSummaryFragmentTestMock();
        SupportFragmentTestUtil.startFragment(thsCostSummaryFragment);

        mTHSCostSummaryPresenter.onApplyCouponResponse(null,thssdkErrorMock);
    }

}