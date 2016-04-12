package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.handler.MetadataListener;
import com.philips.cdp.prodreg.handler.ProdRegConstants;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.handler.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegisteredResponse;
import com.philips.cdp.prodreg.model.RegisteredResponseData;
import com.philips.cdp.prodreg.model.RegistrationState;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProduct {

    public static String PRODUCT_REGISTRATION_KEY = "prod_reg_key";
    private final String TAG = getClass() + "";
    private String requestType;
    private Sector sector;
    private Catalog catalog;
    private String locale;
    private RegisteredProductsListener registeredProductsListener;
    private Context mContext;

    public UserProduct(final Sector sector, final Catalog catalog) {
        this.sector = sector;
        this.catalog = catalog;
    }

    public void registerProduct(final Context context, final Product product, final ProdRegListener appListener) {
        setContext(context);
        setRequestType(ProdRegConstants.PRODUCT_REGISTRATION);
        final User mUser = getUser(context);
        RegisteredProduct registeredProduct = mapProductToRegisteredProduct(product);
        LocalRegisteredProducts localRegisteredProducts = getLocalRegisteredProducts(context);
        localRegisteredProducts.storeProductLocally(registeredProduct);
        registerCachedProducts(context, mUser, localRegisteredProducts.getRegisteredProducts(), appListener);
    }

    @NonNull
    private LocalRegisteredProducts getLocalRegisteredProducts(final Context context) {
        return new LocalRegisteredProducts(context);
    }

    @NonNull
    private User getUser(final Context context) {
        return new User(context);
    }

    private RegisteredProduct mapProductToRegisteredProduct(final Product product) {
        RegisteredProduct registeredProduct = new RegisteredProduct(product.getCtn(), product.getSerialNumber(), product.getPurchaseDate(), product.getSector(), product.getCatalog());
        registeredProduct.setLocale(product.getLocale());
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        return registeredProduct;
    }

    private void registerCachedProducts(final Context context, final User mUser, final List<RegisteredProduct> products, final ProdRegListener appListener) {
        for (Product product : products) {
            Log.d(TAG, product.getCtn() + "___" + product.getSerialNumber());
            if (!isUserSignedIn(mUser, context)) {
                appListener.onProdRegFailed(ProdRegError.USER_NOT_SIGNED_IN);
            } else {
                if (!isValidaDate(product.getPurchaseDate())) {
                    appListener.onProdRegFailed(ProdRegError.INVALID_DATE);
                } else {
                    UserProduct userProduct = getUserProduct();
                    userProduct.setLocale(this.locale);
                    userProduct.getRegisteredProducts(context, getRegisteredProductsListener(context, product, appListener));
                }
            }
        }
    }

    public void getRegisteredProducts(final Context context, final RegisteredProductsListener registeredProductsListener) {
        setContext(context);
        setRequestType(ProdRegConstants.FETCH_REGISTERED_PRODUCTS);
        this.registeredProductsListener = registeredProductsListener;
        RegisteredProductsRequest registeredProductsRequest = getRegisteredProductsRequest(context);
        final RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(registeredProductsRequest, getPrxResponseListenerForRegisteredProducts(registeredProductsListener));
    }

    protected void handleError(final Product product, final int statusCode, final ProdRegListener appListener) {
        if (statusCode == ProdRegError.INVALID_CTN.getCode()) {
            appListener.onProdRegFailed(ProdRegError.INVALID_CTN);
        } else if (statusCode == ProdRegError.USER_TOKEN_EXPIRED.getCode()) {
            getUserProduct().onAccessTokenExpire(product, appListener);
        } else if (statusCode == ProdRegError.ACCESS_TOKEN_INVALID.getCode()) {
            getUserProduct().onAccessTokenExpire(product, appListener);
        } else if (statusCode == ProdRegError.INVALID_VALIDATION.getCode()) {
            appListener.onProdRegFailed(ProdRegError.INVALID_VALIDATION);
        } else if (statusCode == ProdRegError.INVALID_SERIALNUMBER.getCode()) {
            appListener.onProdRegFailed(ProdRegError.INVALID_SERIALNUMBER);
        } else if (statusCode == ProdRegError.NO_INTERNET_AVAILABLE.getCode()) {
            appListener.onProdRegFailed(ProdRegError.NO_INTERNET_AVAILABLE);
        } else if (statusCode == ProdRegError.INTERNAL_SERVER_ERROR.getCode()) {
            appListener.onProdRegFailed(ProdRegError.INTERNAL_SERVER_ERROR);
        } else if (statusCode == ProdRegError.METADATA_FAILED.getCode()) {
            appListener.onProdRegFailed(ProdRegError.METADATA_FAILED);
        } else {
            appListener.onProdRegFailed(ProdRegError.UNKNOWN);
        }
    }

    @NonNull
    protected RegisteredProductsRequest getRegisteredProductsRequest(final Context context) {
        RegisteredProductsRequest registeredProductsRequest = new RegisteredProductsRequest();
        registeredProductsRequest.setSector(getSector());
        registeredProductsRequest.setCatalog(getCatalog());
        registeredProductsRequest.setAccessToken(getUser(context).getAccessToken());
        registeredProductsRequest.setmLocale(getLocale());
        return registeredProductsRequest;
    }

    @NonNull
    protected RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    protected boolean isUserSignedIn(final User mUser, final Context context) {
        return mUser.isUserSignIn(context) && mUser.getEmailVerificationStatus(context);
    }

    protected boolean isValidaDate(final String date) {
        if (date != null && date.length() != 0) {
            String[] dates = date.split("-");
            return dates.length > 1 && Integer.parseInt(dates[0]) > 1999;
        }
        return true;
    }

    @NonNull
    UserProduct getUserProduct() {
        return this;
    }

    String getRequestType() {
        return requestType;
    }

    private void setRequestType(final String requestType) {
        this.requestType = requestType;
    }

    @NonNull
    RegisteredProductsListener getRegisteredProductsListener(final Context context, final Product product, final ProdRegListener appListener) {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProducts(final RegisteredResponse registeredDataResponse) {
                RegisteredResponseData[] results = registeredDataResponse.getResults();
                if (!isCtnRegistered(results, product, appListener))
                    product.getProductMetadata(context, getMetadataListener(context, product, appListener));
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getUserProduct().handleError(product, responseCode, appListener);
            }
        };
    }

    @NonNull
    MetadataListener getMetadataListener(final Context mContext, final Product product, final ProdRegListener appListener) {
        return new MetadataListener() {
            @Override
            public void onMetadataResponse(final ProductMetadataResponse productMetadataResponse) {
                ProductMetadataResponseData productData = productMetadataResponse.getData();
                if (validateSerialNumberFromMetadata(productData, product, appListener)
                        && validatePurchaseDateFromMetadata(productData, product, appListener))

                    getUserProduct().makeRegistrationRequest(mContext, product, appListener);
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getUserProduct().handleError(product, responseCode, appListener);
            }
        };
    }

    protected boolean validatePurchaseDateFromMetadata(final ProductMetadataResponseData data, final Product product, final ProdRegListener listener) {
        final String purchaseDate = product.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                listener.onProdRegFailed(ProdRegError.MISSING_DATE);
                return false;
            }
        } else
            product.setPurchaseDate(null);
        return true;
    }

    protected boolean isCtnRegistered(final RegisteredResponseData[] results, final Product product, final ProdRegListener listener) {
        for (RegisteredResponseData result : results) {
            if (product.getCtn().equalsIgnoreCase(result.getProductModelNumber())) {
                listener.onProdRegFailed(ProdRegError.PRODUCT_ALREADY_REGISTERED);
                return true;
            }
        }
        return false;
    }

    protected boolean validateSerialNumberFromMetadata(final ProductMetadataResponseData data, final Product product, final ProdRegListener listener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, product, listener)) return false;
        } else {
            product.setSerialNumber(null);
        }
        return true;
    }

    @NonNull
    protected RegistrationRequest getRegistrationRequest(final Context context, final Product product) {
        RegistrationRequest registrationRequest = new RegistrationRequest(product.getCtn(), product.getSerialNumber(), getUser(context).getAccessToken());
        registrationRequest.setSector(product.getSector());
        registrationRequest.setCatalog(product.getCatalog());
        registrationRequest.setmLocale(product.getLocale());
        registrationRequest.setRegistrationChannel(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setPurchaseDate(product.getPurchaseDate());
        return registrationRequest;
    }

    protected void onAccessTokenExpire(final Product product, final ProdRegListener appListener) {
        final User user = getUser(mContext);
        user.refreshLoginSession(getRefreshLoginSessionHandler(product, appListener, mContext), mContext);
    }

    @NonNull
    protected RefreshLoginSessionHandler getRefreshLoginSessionHandler(final Product product, final ProdRegListener appListener, final Context mContext) {
        return new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                getUserProduct().retryRequests(mContext, product, appListener);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int error) {
                Log.d(TAG, "error in refreshing session");
                appListener.onProdRegFailed(ProdRegError.REFRESH_ACCESS_TOKEN_FAILED);
            }
        };
    }

    protected void retryRequests(final Context mContext, final Product product, final ProdRegListener appListener) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                getUserProduct().makeRegistrationRequest(mContext, product, appListener);
                break;
            case ProdRegConstants.FETCH_REGISTERED_PRODUCTS:
                getUserProduct().getRegisteredProducts(mContext, getRegisteredProductsListener());
                break;
            default:
                break;
        }
    }

    @NonNull
    ResponseListener getPrxResponseListenerForRegisteredProducts(final RegisteredProductsListener registeredProductsListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                RegisteredResponse registeredDataResponse = (RegisteredResponse) responseData;
                registeredProductsListener.getRegisteredProducts(registeredDataResponse);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    registeredProductsListener.onErrorResponse(errorMessage, responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @NonNull
    ResponseListener getPrxResponseListener(final Product product, final ProdRegListener appListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                appListener.onProdRegSuccess(responseData);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    getUserProduct().handleError(product, responseCode, appListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    protected void makeRegistrationRequest(final Context mContext, final Product product, final ProdRegListener appListener) {
        this.requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        RegistrationRequest registrationRequest = getRegistrationRequest(mContext, product);
        registrationRequest.setRegistrationChannel(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setmLocale(product.getLocale());
        registrationRequest.setPurchaseDate(product.getPurchaseDate());
        registrationRequest.setProductSerialNumber(product.getSerialNumber());
        RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registrationRequest, getPrxResponseListener(product, appListener));
    }

    public RegisteredProductsListener getRegisteredProductsListener() {
        return registeredProductsListener;
    }

    public Sector getSector() {
        return sector;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    private boolean processSerialNumber(final ProductMetadataResponseData data, final Product product, final ProdRegListener listener) {
        if (product.getSerialNumber() == null || product.getSerialNumber().length() < 1) {
            listener.onProdRegFailed(ProdRegError.MISSING_SERIALNUMBER);
            return true;
        } else if (!product.getSerialNumber().matches(data.getSerialNumberFormat())) {
            listener.onProdRegFailed(ProdRegError.INVALID_SERIALNUMBER);
            return true;
        }
        return false;
    }

    private void setContext(final Context context) {
        this.mContext = context;
    }
}
