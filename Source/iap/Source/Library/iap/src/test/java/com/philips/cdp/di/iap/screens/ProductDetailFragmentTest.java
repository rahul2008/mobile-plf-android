package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.cdp.di.iap.utils.IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ProductDetailFragmentTest {
    private Context mContext;
    ProductDetailFragment productDetailFragment;

    @Before
    public void setUp() {
        initMocks(this);
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        isNetworkAvailable();

        mContext = RuntimeEnvironment.application;
        productDetailFragment = ProductDetailFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(productDetailFragment);
    }

    @Mock
    View viewMock;



    @Mock
    ProgressBarButton progressBarAddToCartButtonMock;

    @Mock
    ProgressBarButton progressBarBuyFromRetailerButtonMock;

    @Mock
    LinearLayout linearLayout1Mock;

    @Mock
    LinearLayout linearLayout2Mock;

    @Mock
    Button button;

    @Mock
    ViewPager viewPagerMock;

    @Mock
    DotNavigationIndicator dotNavigationIndicatorMock;

    @Mock
    ScrollView scrollViewMock;
    @Mock
    TextView productDescriptionMock;

    @Mock
    TextView ctnMock;

    @Mock
    TextView priceMock;

    @Mock
    TextView textView;

    @Mock
    TextView textView1;

    @Mock
    TextView textView2;

    @Mock
    TextView textView3;

    @Mock
    TextView productOverviewMock;

    /*
  *
  *     mDetailLayout = (ScrollView) rootView.findViewById(R.id.scrollView);
      mProductDescription = (TextView) rootView.findViewById(R.id.iap_productDetailScreen_productDescription_lebel);
      mCTN = (TextView) rootView.findViewById(R.id.iap_productDetailsScreen_ctn_lebel);
      mPrice = (TextView) rootView.findViewById(R.id.iap_productDetailsScreen_individualPrice_lebel);
      mProductOverview = (TextView) rootView.findViewById(R.id.iap_productDetailsScreen_productOverview);
      mAddToCart = (ProgressBarButton) rootView.findViewById(R.id.iap_productDetailsScreen_addToCart_button);
      mBuyFromRetailers = (ProgressBarButton) rootView.findViewById(R.id.iap_productDetailsScreen_buyFromRetailor_button);
      mCheckutAndCountinue = (LinearLayout) rootView.findViewById(R.id.iap_productDetailsScreen_btn_ll);
      mQuantityAndDelete = (LinearLayout) rootView.findViewById(R.id.iap_productDetailsScreen_quantity_delete_btn_ll);
      mProductDiscountedPrice = (TextView) rootView.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel);
      mProductStockInfo = (TextView) rootView.findViewById(R.id.iap_productDetailsScreen_outOfStock_label);
      mDeleteProduct = (Button) rootView.findViewById(delete_btn);
      mDeleteProduct.setOnClickListener(this);
      mQuantity = (TextView) rootView.findViewById(R.id.quantity_val);
      mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
      DotNavigationIndicator indicator = (DotNavigationIndicator) rootView.findViewById(R.id.indicator);
  *
  * */

    @Test
    public void shouldInitializeViews() throws Exception {
        initializaViews();
        productDetailFragment.initializeViews(viewMock);
    }

    @Test
    public void shouldInitializeViewsWithBundleArgument() throws Exception {
        initializaViews();
        Bundle bundle=new Bundle();
        bundle.putString(IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,"X234");
        productDetailFragment.setArguments(bundle);
        productDetailFragment.initializeViews(viewMock);
    }

    void initializaViews(){
        Mockito.when(viewMock.findViewById(R.id.scrollView)).thenReturn(scrollViewMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailScreen_productDescription_lebel)).thenReturn(productDescriptionMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_ctn_lebel)).thenReturn(ctnMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_individualPrice_lebel)).thenReturn(priceMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_addToCart_button)).thenReturn(progressBarAddToCartButtonMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_buyFromRetailor_button)).thenReturn(progressBarBuyFromRetailerButtonMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_btn_ll)).thenReturn(linearLayout1Mock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_quantity_delete_btn_ll)).thenReturn(linearLayout2Mock);
        Mockito.when(viewMock.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel)).thenReturn(textView);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_outOfStock_label)).thenReturn(textView1);
        Mockito.when(viewMock.findViewById(R.id.delete_btn)).thenReturn(button);
        Mockito.when(viewMock.findViewById(R.id.quantity_val)).thenReturn(textView2);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_productOverview)).thenReturn(textView3);
        Mockito.when(viewMock.findViewById(R.id.pager)).thenReturn(viewPagerMock);
        Mockito.when(viewMock.findViewById(R.id.indicator)).thenReturn(dotNavigationIndicatorMock);

        /* TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        int mThemeBaseColor = a.getColor(0, ContextCompat.getColor(context, R.color.uikit_philips_blue));
        a.recycle();*/

        productDetailFragment.onAttach(mContext);
    }

    public void isNetworkAvailable() {
        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCallOnResume() throws Exception {
        initializaViews();
        Bundle bundle=new Bundle();
       // bundle.putString(IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,"X234");
        productDetailFragment.setArguments(bundle);
        productDetailFragment.initializeViews(viewMock);
        productDetailFragment.onResume();

    }


/*    public void callOnResume_WhenmLaunchedFromProductCatalogIsTrue() throws Exception {

        initializaViews();
        Bundle bundle=new Bundle();
        bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG,true);
        productDetailFragment.setArguments(bundle);
        productDetailFragment.initializeViews(viewMock);
        productDetailFragment.onResume();
    }*/
}