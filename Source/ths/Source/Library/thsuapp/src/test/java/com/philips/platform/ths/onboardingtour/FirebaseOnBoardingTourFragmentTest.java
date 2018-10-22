package com.philips.platform.ths.onboardingtour;

import android.view.View;

import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_1;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_2;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_3;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_4;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class FirebaseOnBoardingTourFragmentTest {

    private FirebaseOnBoardingTourFragment firebaseOnboardingTourFragment;

    @Mock
    private OnBoardingTourPresenter mOnBoardingTourPresenterMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        firebaseOnboardingTourFragment = new FirebaseOnBoardingTourFragment();
        SupportFragmentTestUtil.startFragment(firebaseOnboardingTourFragment);
    }

    @Test
    public void shouldCreateOnBoardingTourContentModelList_whenCreated() throws Exception {
        firebaseOnboardingTourFragment.onCreate(null);

        assertThat(firebaseOnboardingTourFragment.onBoardingTourContentModelList.get(0).getPageTitle()).isEqualTo(ON_BOARDING_PAGE_1);
        assertThat(firebaseOnboardingTourFragment.onBoardingTourContentModelList.get(1).getPageTitle()).isEqualTo(ON_BOARDING_PAGE_2);
        assertThat(firebaseOnboardingTourFragment.onBoardingTourContentModelList.get(2).getPageTitle()).isEqualTo(ON_BOARDING_PAGE_3);
        assertThat(firebaseOnboardingTourFragment.onBoardingTourContentModelList.get(3).getPageTitle()).isEqualTo(ON_BOARDING_PAGE_4);
        assertThat(firebaseOnboardingTourFragment.onBoardingTourContentModelList.get(4).getPageTitle()).isEqualTo(ON_BOARDING_PAGE_5);
    }

    @Test
    public void updateViews_WhenPageChanged() throws Exception {
        firebaseOnboardingTourFragment.pager.setCurrentItem(4);
        if (firebaseOnboardingTourFragment.getView() != null) {
            assertThat(firebaseOnboardingTourFragment.getView().findViewById(R.id.btn_startnow).getVisibility()).isEqualTo(View.GONE);
            assertThat(firebaseOnboardingTourFragment.getView().findViewById(R.id.btn_termsConditions).getVisibility()).isEqualTo(View.VISIBLE);
        }

        firebaseOnboardingTourFragment.pager.setCurrentItem(2);
        if (firebaseOnboardingTourFragment.getView() != null) {
            assertThat(firebaseOnboardingTourFragment.getView().findViewById(R.id.btn_startnow).getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(firebaseOnboardingTourFragment.getView().findViewById(R.id.btn_termsConditions).getVisibility()).isEqualTo(View.GONE);
        }
    }


    @Test
    public void shouldDelegateToPresenter_WhenButtonClicked() throws Exception {
        firebaseOnboardingTourFragment.presenter = mOnBoardingTourPresenterMock;
        if (firebaseOnboardingTourFragment.getView() != null) {
            final View viewById = firebaseOnboardingTourFragment.getView().findViewById(R.id.btn_termsConditions);
            viewById.performClick();
        }
        verify(mOnBoardingTourPresenterMock).onEvent(anyInt());
    }

    @Test
    public void shouldReturnAdapter_whenAsked() throws Exception {
       assertThat(firebaseOnboardingTourFragment.getOnBoardingTourPagerAdapter()).isNotNull();
    }

    @Test
    public void shouldReturnTrue_OnExit() throws Exception {
        assertThat(firebaseOnboardingTourFragment.handleBackEvent()).isFalse();
    }

    @Test
    public void test_setCurrent() throws Exception {
        firebaseOnboardingTourFragment.setCurrent();
        assert firebaseOnboardingTourFragment.pager.getCurrentItem()==4;
    }

}
