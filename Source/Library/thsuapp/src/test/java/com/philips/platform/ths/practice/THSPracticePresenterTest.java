package com.philips.platform.ths.practice;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerType;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.intake.THSFollowUpFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by philips on 6/27/17.
 */
public class THSPracticePresenterTest {

    Method mMethod;
    THSPracticeFragment mUIBaseView;
    THSPracticePresenter mTHSPracticePresenter;
    Consumer mConsumer;

    @Mock
    Context contextMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    AWSDKFactory awsdkFactoryMock;

    @Mock
    SDKError sdkError;

    @Mock
    Throwable throwable;

    private ArgumentCaptor<THSPracticesListCallback> requestCaptor;



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        mConsumer = new Consumer() {
            @Override
            public Gender getGender() {
                return Gender.MALE;
            }

            @Override
            public String getAge() {
                return "34";
            }

            @Override
            public String getFormularyRestriction() {
                return null;
            }

            @Override
            public boolean isEligibleForVisit() {
                return false;
            }

            @Override
            public boolean isEnrolled() {
                return true;
            }

            @Override
            public Subscription getSubscription() {
                return null;
            }

            @Override
            public String getPhone() {
                return "7899673388";
            }

            @Override
            public List<Consumer> getDependents() {
                return null;
            }

            @Override
            public boolean isDependent() {
                return false;
            }

            @Override
            public boolean isAppointmentReminderTextsEnabled() {
                return false;
            }

            @Override
            public SDKLocalDate getDob() {
                return null;
            }

            @Override
            public Address getAddress() {
                return null;
            }

            @Override
            public ConsumerType getConsumerType() {
                return null;
            }

            @Override
            public String getEmail() {
                return null;
            }

            @Override
            public State getLegalResidence() {
                return null;
            }

            @NonNull
            @Override
            public String getFirstName() {
                return "Ravi";
            }

            @Nullable
            @Override
            public String getMiddleInitial() {
                return null;
            }

            @NonNull
            @Override
            public String getLastName() {
                return "Kumar";
            }

            @NonNull
            @Override
            public String getFullName() {
                return "Ravi Kumar";
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        mUIBaseView = new THSPracticeFragment();
        mTHSPracticePresenter = new THSPracticePresenter(mUIBaseView);
        assertNotNull(mTHSPracticePresenter);


    }



    @Test
    public void fetchPracticeTestForNull()  {
        try {
            mMethod = THSPracticePresenter.class.getDeclaredMethod("fetchPractices");
            mMethod.setAccessible(true);
            mMethod.invoke(mTHSPracticePresenter);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }



}