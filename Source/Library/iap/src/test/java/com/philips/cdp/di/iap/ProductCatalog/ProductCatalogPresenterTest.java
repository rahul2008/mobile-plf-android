package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.net.ssl.SSLSocketFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductCatalogPresenterTest {

    @Mock
    private NetworkController mNetworkController;
    @Mock
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private ProductCatalogPresenter mPresenter;
    @Mock
    private Context context;
    @Mock
    private IAPJsonRequest mIAPJsonReq;
    @Captor
    private ArgumentCaptor<RequestListener> mRequestListener;
    @Mock
    private SSLSocketFactory mSocketFactory;
    @Mock
    private Message mResultMessage;
    @Mock
    private FragmentManager mFragmentManager;

    @Before
    public void setUP() {
        mPresenter = new ProductCatalogPresenter(context, null, mFragmentManager);
        mPresenter.setHybrisDelegate(mHybrisDelegate);
        when(mHybrisDelegate.getNetworkController(context)).thenReturn(mNetworkController);
        doNothing().when(mNetworkController).addToVolleyQueue(mIAPJsonReq);
    }

    @Test
    public void doSomething() {
        mPresenter.getProductCatalog();
        Store store = Mockito.mock(Store.class);
        GetProductCatalogRequest model = new GetProductCatalogRequest(store, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(Mockito.mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }
}