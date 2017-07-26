/*
package com.philips.platform.ths.providerdetails;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProviderListPresenter;
import com.philips.platform.ths.providerslist.THSProviderListViewInterface;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSProviderDetailsDisplayHelperTest {

    private THSLaunchActivity mActivity;
    private THSProviderDetailsFragment providerDetailsFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    View view;

    @Mock
    ActionBarListener actionBarListener;

    @Mock
    FragmentLauncher framgmentLauncherMock;

    @Mock
    ProviderType providerTypeMock;

    @Mock
    THSProviderListViewInterface THSProviderListViewInterface;

    @InjectMocks
    THSProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumerMock;
    @Mock
    ProviderInfo providerInfo;

    @Mock
    Practice practiceMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    THSBaseView THSBaseView;

    @Mock
    Provider providerMock;

    THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelper;

    @Mock
    THSProviderEntity thsProviderEntityMock;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    THSAvailableProvider thsAvailableProviderInfoMock;

    @Mock
    List listMock;

    @Mock
    Context context;

    @Mock
    THSProviderDetailsPresenter presenterMock;

    @Mock
    ImageView providerImage,isAvailableImage;
    @Mock
    Label providerName,practiceName,isAvailable,spokenLanguageValueLabel,yearsOfExpValueLabel,graduatedValueLabel,aboutMeValueLabel;

    @Mock
    RatingBar providerRating;
    @Mock
     Button detailsButtonOne,detailsButtonTwo,detailsButtonContinue;
    @Mock
     RelativeLayout mTimeSlotContainer;
    @Mock
     THSExpandableHeightGridView gridView;
    @Mock
     SwipeRefreshLayout swipeRefreshLayout;

    @Mock
    Language languageOneMock;

    @Mock
    Language languageTwoMock;

    @Mock
    Date dateMock;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        when(THSBaseView.getFragmentActivity()).thenReturn(mActivity);
        when(THSManager.getInstance().getAwsdk(mActivity).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsFragment = new THSProviderDetailsFragment();
        providerDetailsFragment.setConsumerAndPractice(consumerMock, practiceMock);
        providerDetailsFragment.setActionBarListener(actionBarListener);
        thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(context,providerDetailsFragment,providerDetailsFragment,providerDetailsFragment,thsBaseFragmentMock, view);

    }

    void setViews() {
        thsProviderDetailsDisplayHelper.swipeRefreshLayout = swipeRefreshLayout;
        thsProviderDetailsDisplayHelper.providerImage = providerImage;
        thsProviderDetailsDisplayHelper.providerName = providerName;
        thsProviderDetailsDisplayHelper.practiceName = practiceName;
        thsProviderDetailsDisplayHelper.isAvailable = isAvailable;
        thsProviderDetailsDisplayHelper.isAvailableImage = isAvailableImage;
        thsProviderDetailsDisplayHelper.providerRating = providerRating;
        thsProviderDetailsDisplayHelper.spokenLanguageValueLabel = spokenLanguageValueLabel;
        thsProviderDetailsDisplayHelper.yearsOfExpValueLabel = yearsOfExpValueLabel;
        thsProviderDetailsDisplayHelper.graduatedValueLabel = graduatedValueLabel;
        thsProviderDetailsDisplayHelper.aboutMeValueLabel = aboutMeValueLabel;
        thsProviderDetailsDisplayHelper.detailsButtonOne = detailsButtonOne;
        thsProviderDetailsDisplayHelper.detailsButtonTwo = detailsButtonTwo;
        thsProviderDetailsDisplayHelper.detailsButtonContinue = detailsButtonContinue;
        thsProviderDetailsDisplayHelper.gridView = gridView;


        thsProviderDetailsDisplayHelper.mTimeSlotContainer = mTimeSlotContainer;
    }


    @Test
    public  void testFragment(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        assertNotNull(providerDetailsFragment);
    }

    @Test
    public void updateView(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.OFFLINE);
        List list = new ArrayList();
        list.add(languageOneMock);
        list.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list);

        setViews();

        List list1 = new ArrayList();
        list1.add(dateMock);
        thsProviderDetailsDisplayHelper.updateView(providerMock,list1);
        verify(providerName).setText("Spoorti");
    }

    @Test(expected = AssertionError.class)
    public void updateViewBasedOnType(){
        THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        thsAvailableProviderDetailFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        List list = new ArrayList();
        list.add(dateMock);
        when(thsAvailableProviderInfoMock.getAvailableAppointmentTimeSlots()).thenReturn(list);

        thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(context, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsBaseFragmentMock,view);
        setViews();

        List list1 = new ArrayList();
        list1.add(dateMock);
        thsProviderDetailsDisplayHelper.updateViewBasedOnType(providerMock,list);
        verify(mTimeSlotContainer).setVisibility(View.VISIBLE);
    }

    @Test
    public void updateViewisOnCall(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.ON_CALL);
        List list = new ArrayList();
        list.add(languageOneMock);
        list.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list);

        setViews();

        List list1 = new ArrayList();
        list1.add(dateMock);
        thsProviderDetailsDisplayHelper.updateView(providerMock,dateMock);
        verify(providerName).setText("Spoorti");
    }

    @Test(expected = AssertionError.class)
    public void updateViewisOnCallisAvailableProviderData(){

        THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        thsAvailableProviderDetailFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        List list = new ArrayList();
        list.add(dateMock);
        when(thsAvailableProviderInfoMock.getAvailableAppointmentTimeSlots()).thenReturn(list);

        thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(context, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsBaseFragmentMock,view);

    //    SupportFragmentTestUtil.startFragment(thsAvailableProviderDetailFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.ON_CALL);
        List list1 = new ArrayList();
        list1.add(languageOneMock);
        list1.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list1);

        setViews();

        List list2 = new ArrayList();
        list2.add(dateMock);

        thsProviderDetailsDisplayHelper.updateView(providerMock,list2);
        verify(providerName).setText("Spoorti");
    }

    @Test
    public void updateViewIsVideoAvailable(){
        SupportFragmentTestUtil.startFragment(providerDetailsFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.WEB_AVAILABLE);
        List list = new ArrayList();
        list.add(languageOneMock);
        list.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list);

        setViews();

        thsProviderDetailsDisplayHelper.updateView(providerMock);
        verify(providerName).setText("Spoorti");
    }

    @Test(expected = AssertionError.class)
    public void updateViewWEB_AVAILABLEisAvailableProviderData(){

        THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        thsAvailableProviderDetailFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        List list = new ArrayList();
        list.add(dateMock);
        when(thsAvailableProviderInfoMock.getAvailableAppointmentTimeSlots()).thenReturn(list);

        thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(context, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsBaseFragmentMock,view);

        //    SupportFragmentTestUtil.startFragment(thsAvailableProviderDetailFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.WEB_AVAILABLE);
        List list1 = new ArrayList();
        list1.add(languageOneMock);
        list1.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list1);

        setViews();
        thsProviderDetailsDisplayHelper.updateView(providerMock);
        verify(providerName).setText("Spoorti");
    }

    @Test(expected = AssertionError.class)
    public void updateViewOfflineisAvailableProviderData(){

        THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        thsAvailableProviderDetailFragment.mThsAvailableProvider = thsAvailableProviderInfoMock;
        List list = new ArrayList();
        list.add(dateMock);
        when(thsAvailableProviderInfoMock.getAvailableAppointmentTimeSlots()).thenReturn(list);

        thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(context, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsAvailableProviderDetailFragment, thsBaseFragmentMock,view);

        //    SupportFragmentTestUtil.startFragment(thsAvailableProviderDetailFragment);
        when(providerMock.getFullName()).thenReturn("Spoorti");

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getSpokenLanguages()).thenReturn(listMock);
        when(providerMock.getYearsExperience()).thenReturn(3);
        when(providerMock.getSchoolName()).thenReturn("Spoorti");
        when(providerMock.getTextGreeting()).thenReturn("HI");
        when(providerMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("Spoorti");
        when(providerMock.getVisibility()).thenReturn(ProviderVisibility.OFFLINE);
        List list1 = new ArrayList();
        list1.add(languageOneMock);
        list1.add(languageTwoMock);
        //when(listMock.get(0)).thenReturn(languageOneMock);
        when(languageOneMock.getName()).thenReturn("ENG");
        when(languageTwoMock.getName()).thenReturn("SCE");
        when(providerMock.getSpokenLanguages()).thenReturn(list1);

        setViews();
        thsProviderDetailsDisplayHelper.updateView(providerMock);
        verify(providerName).setText("Spoorti");
    }

}
*/
