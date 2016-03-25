package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.PRXProductAssetBuilder;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.net.ssl.SSLSocketFactory;

import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class PRXProductAssetBuilderTest {

    @Mock
    private Context context;
    @Mock
    private PRXProductAssetBuilder mPrxProductAssetBuilder;
    @Mock
    private ProductAssetBuilder mProductAssetBuilder;



    @Before
    public void setUP() {
        mPrxProductAssetBuilder = Mockito.mock(PRXProductAssetBuilder.class);
    }

    @Test
    public void executeRequest(){
        mPrxProductAssetBuilder.executeRequest(mProductAssetBuilder);
        verify(mPrxProductAssetBuilder).executeRequest(mProductAssetBuilder);
    }

}