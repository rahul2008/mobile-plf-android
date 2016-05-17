package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPJsonRequest;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.HybrisStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.net.ssl.SSLSocketFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartPresenterTest {

    @Mock
    private NetworkController mNetworkController;
    @Mock
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private ShoppingCartPresenter mPresenter;
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
    @Mock
    private ShoppingCartData mShoppingCartData;
    @Mock
    private IAPCartListener mIapCartListener;

    String mProductCTN = "HX8071/10";

    @Before
    public void setUP() {
        mPresenter = new ShoppingCartPresenter(context, mock(ShoppingCartPresenter.LoadListener
                .class), mFragmentManager);
        mPresenter.setHybrisDelegate(mHybrisDelegate);
        when(mHybrisDelegate.getNetworkController(context)).thenReturn(mNetworkController);
        doNothing().when(mNetworkController).addToVolleyQueue(mIAPJsonReq);
    }

    @Test
    public void getCurrentCartDetails() {
        mPresenter.getCurrentCartDetails();
        HybrisStore hybrisStore = mock(HybrisStore.class);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(hybrisStore, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }

    @Test
    public void getRetailersInformation() {
        mPresenter.getRetailersInformation(mProductCTN);
        HybrisStore hybrisStore = mock(HybrisStore.class);
        GetRetailersInfoRequest model = new GetRetailersInfoRequest(hybrisStore, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }

    @Test
    public void deleteProduct() {
        mPresenter.deleteProduct(mShoppingCartData);
        HybrisStore hybrisStore = mock(HybrisStore.class);
        CartDeleteProductRequest model = new CartDeleteProductRequest(hybrisStore, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }

    @Test
    public void updateProductQuantityIncrease() {
        int count = 5;
        int quantityStatus = 1;//Increasing Quantity
        mPresenter.updateProductQuantity(mShoppingCartData, count, quantityStatus);
        HybrisStore hybrisStore = mock(HybrisStore.class);
        CartUpdateProductQuantityRequest model = new CartUpdateProductQuantityRequest(hybrisStore, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }

    @Test
    public void updateProductQuantityDecrease() {
        int count = 1;
        int quantityStatus = 0;//Decreasing Quantity
        mPresenter.updateProductQuantity(mShoppingCartData, count, quantityStatus);
        HybrisStore hybrisStore = mock(HybrisStore.class);
        CartUpdateProductQuantityRequest model = new CartUpdateProductQuantityRequest(hybrisStore, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        model.setContext(mock(Context.class));
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mRequestListener.capture());
    }

    public void setHybrisDelegate() {
        mPresenter.setHybrisDelegate(mHybrisDelegate);
    }

    @Test
    public void createCart(){
        StoreSpec store = new MockStore(context, mock(IAPUser.class)).getStore();
        CartCreateRequest model = new CartCreateRequest(store, null,
                    new AbstractModel.DataLoadListener() {
                        @Override
                        public void onModelDataLoadFinished(final Message msg) {
                        }

                        @Override
                        public void onModelDataError(final Message msg) {
                        }
                    });
        mHybrisDelegate.sendRequest(RequestCode.DELETE_ADDRESS, model, model);

    }

    @Test
    public void addProductToCartFromBuyNow(){
        StoreSpec store = new MockStore(context, mock(IAPUser.class)).getStore();
        CartAddProductRequest model = new CartAddProductRequest(store, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        mHybrisDelegate.sendRequest(RequestCode.DELETE_ADDRESS, model, model);

    }

    @Test
    public void getProductCartCount(){
        StoreSpec store = new MockStore(context, mock(IAPUser.class)).getStore();
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(store, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        mHybrisDelegate.sendRequest(RequestCode.DELETE_ADDRESS, model, model);

    }

    @Test
    public void buyProduct(){
        StoreSpec store = new MockStore(context, mock(IAPUser.class)).getStore();
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(store, null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                    }
                });
        mHybrisDelegate.sendRequest(RequestCode.DELETE_ADDRESS, model, model);

    }
}