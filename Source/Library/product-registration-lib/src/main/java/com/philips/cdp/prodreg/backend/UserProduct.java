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
        LocalRegisteredProducts localRegisteredProducts = getLocalRegisteredProductsInstance(context);
        localRegisteredProducts.storeProductLocally(registeredProduct);
        registerCachedProducts(context, mUser, localRegisteredProducts.getRegisteredProducts(), appListener);
    }

    @NonNull
    private LocalRegisteredProducts getLocalRegisteredProductsInstance(final Context context) {
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

    private void registerCachedProducts(final Context context, final User mUser, final List<RegisteredProduct> registeredProducts, final ProdRegListener appListener) {
        for (RegisteredProduct registeredProduct : registeredProducts) {
            Log.d(TAG, registeredProduct.getCtn() + "___" + registeredProduct.getSerialNumber());
            if (registeredProduct.getRegistrationState() == RegistrationState.PENDING) {
                if (!isUserSignedIn(mUser, context)) {
                    registeredProduct.setRegistrationState(RegistrationState.PENDING);
                    registeredProduct.setProdRegError(ProdRegError.USER_NOT_SIGNED_IN);
                    getLocalRegisteredProductsInstance(context).updateRegisteredProducts(registeredProduct);
                    appListener.onProdRegFailed(ProdRegError.USER_NOT_SIGNED_IN);
                } else {
                    if (!isValidaDate(registeredProduct.getPurchaseDate())) {
                        registeredProduct.setRegistrationState(RegistrationState.FAILED);
                        registeredProduct.setProdRegError(ProdRegError.INVALID_DATE);
                        getLocalRegisteredProductsInstance(context).updateRegisteredProducts(registeredProduct);
                        appListener.onProdRegFailed(ProdRegError.INVALID_DATE);
                    } else {
                        UserProduct userProduct = getUserProduct();
                        userProduct.setLocale(this.locale);
                        userProduct.getRegisteredProducts(context, getRegisteredProductsListener(context, registeredProduct, appListener));
                    }
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

    protected void handleError(final RegisteredProduct registeredProduct, final int statusCode, final ProdRegListener appListener) {
        if (statusCode == ProdRegError.INVALID_CTN.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_CTN, RegistrationState.FAILED);
            appListener.onProdRegFailed(ProdRegError.INVALID_CTN);
        } else if (statusCode == ProdRegError.USER_TOKEN_EXPIRED.getCode()) {
            getUserProduct().onAccessTokenExpire(registeredProduct, appListener);
        } else if (statusCode == ProdRegError.ACCESS_TOKEN_INVALID.getCode()) {
            getUserProduct().onAccessTokenExpire(registeredProduct, appListener);
        } else if (statusCode == ProdRegError.INVALID_VALIDATION.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);
            appListener.onProdRegFailed(ProdRegError.INVALID_VALIDATION);
        } else if (statusCode == ProdRegError.INVALID_SERIALNUMBER.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            appListener.onProdRegFailed(ProdRegError.INVALID_SERIALNUMBER);
        } else if (statusCode == ProdRegError.NO_INTERNET_AVAILABLE.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);
            appListener.onProdRegFailed(ProdRegError.NO_INTERNET_AVAILABLE);
        } else if (statusCode == ProdRegError.INTERNAL_SERVER_ERROR.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.PENDING);
            appListener.onProdRegFailed(ProdRegError.INTERNAL_SERVER_ERROR);
        } else if (statusCode == ProdRegError.METADATA_FAILED.getCode()) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.METADATA_FAILED, RegistrationState.FAILED);
            appListener.onProdRegFailed(ProdRegError.METADATA_FAILED);
        } else {
            appListener.onProdRegFailed(ProdRegError.UNKNOWN);
        }
    }

    private void updateLocaleCacheOnError(final RegisteredProduct registeredProduct, final ProdRegError prodRegError, final RegistrationState registrationState) {
        registeredProduct.setRegistrationState(registrationState);
        registeredProduct.setProdRegError(prodRegError);
        getLocalRegisteredProductsInstance(mContext).updateRegisteredProducts(registeredProduct);
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
    RegisteredProductsListener getRegisteredProductsListener(final Context context, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProducts(final RegisteredResponse registeredDataResponse) {
                RegisteredResponseData[] results = registeredDataResponse.getResults();
                if (!isCtnRegistered(results, registeredProduct, appListener))
                    registeredProduct.getProductMetadata(context, getMetadataListener(context, registeredProduct, appListener));
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getUserProduct().handleError(registeredProduct, responseCode, appListener);
            }
        };
    }

    @NonNull
    MetadataListener getMetadataListener(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        return new MetadataListener() {
            @Override
            public void onMetadataResponse(final ProductMetadataResponse productMetadataResponse) {
                ProductMetadataResponseData productData = productMetadataResponse.getData();
                if (validateSerialNumberFromMetadata(productData, registeredProduct, appListener)
                        && validatePurchaseDateFromMetadata(productData, registeredProduct, appListener))

                    getUserProduct().makeRegistrationRequest(mContext, registeredProduct, appListener);
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getUserProduct().handleError(registeredProduct, responseCode, appListener);
            }
        };
    }

    protected boolean validatePurchaseDateFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        final String purchaseDate = registeredProduct.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_DATE, RegistrationState.FAILED);
                listener.onProdRegFailed(ProdRegError.MISSING_DATE);
                return false;
            }
        } else
            registeredProduct.setPurchaseDate(null);
        return true;
    }

    protected boolean isCtnRegistered(final RegisteredResponseData[] results, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        for (RegisteredResponseData result : results) {
            if (registeredProduct.getCtn().equalsIgnoreCase(result.getProductModelNumber())) {
                updateLocaleCacheOnError(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.FAILED);
                listener.onProdRegFailed(ProdRegError.PRODUCT_ALREADY_REGISTERED);
                return true;
            }
        }
        return false;
    }

    protected boolean validateSerialNumberFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, registeredProduct, appListener)) return false;
        } else {
            registeredProduct.setSerialNumber(null);
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

    protected void onAccessTokenExpire(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        final User user = getUser(mContext);
        user.refreshLoginSession(getRefreshLoginSessionHandler(registeredProduct, appListener, mContext), mContext);
    }

    @NonNull
    protected RefreshLoginSessionHandler getRefreshLoginSessionHandler(final RegisteredProduct registeredProduct, final ProdRegListener appListener, final Context mContext) {
        return new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                getUserProduct().retryRequests(mContext, registeredProduct, appListener);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(final int error) {
                Log.d(TAG, "error in refreshing session");
                registeredProduct.setRegistrationState(RegistrationState.FAILED);
                registeredProduct.setProdRegError(ProdRegError.REFRESH_ACCESS_TOKEN_FAILED);
                getLocalRegisteredProductsInstance(mContext).updateRegisteredProducts(registeredProduct);
                appListener.onProdRegFailed(ProdRegError.REFRESH_ACCESS_TOKEN_FAILED);
            }
        };
    }

    protected void retryRequests(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        switch (requestType) {
            case ProdRegConstants.PRODUCT_REGISTRATION:
                getUserProduct().makeRegistrationRequest(mContext, registeredProduct, appListener);
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
    ResponseListener getPrxResponseListener(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
                registeredProduct.setProdRegError(null);
                getLocalRegisteredProductsInstance(mContext).updateRegisteredProducts(registeredProduct);
                appListener.onProdRegSuccess(responseData);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    getUserProduct().handleError(registeredProduct, responseCode, appListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    protected void makeRegistrationRequest(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        this.requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        RegistrationRequest registrationRequest = getRegistrationRequest(mContext, registeredProduct);
        registrationRequest.setRegistrationChannel(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setmLocale(registeredProduct.getLocale());
        registrationRequest.setPurchaseDate(registeredProduct.getPurchaseDate());
        registrationRequest.setProductSerialNumber(registeredProduct.getSerialNumber());
        RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registrationRequest, getPrxResponseListener(registeredProduct, appListener));
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

    private boolean processSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        if (registeredProduct.getSerialNumber() == null || registeredProduct.getSerialNumber().length() < 1) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(ProdRegError.MISSING_SERIALNUMBER);
            return true;
        } else if (!registeredProduct.getSerialNumber().matches(data.getSerialNumberFormat())) {
            updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(ProdRegError.INVALID_SERIALNUMBER);
            return true;
        }
        return false;
    }

    private void setContext(final Context context) {
        this.mContext = context;
    }
}
