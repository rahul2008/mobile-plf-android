package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.handler.ErrorType;
import com.philips.cdp.prodreg.handler.MetadataListener;
import com.philips.cdp.prodreg.handler.ProdRegConstants;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.handler.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.RegisteredResponse;
import com.philips.cdp.prodreg.model.RegisteredResponseData;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProduct {

    private final String TAG = getClass() + "";
    private Context mContext;
    private String requestType;
    private Sector sector;
    private Catalog catalog;
    private String locale;
    private RegisteredProductsListener registeredProductsListener;

    public UserProduct(final Sector sector, final Catalog catalog) {
        this.sector = sector;
        this.catalog = catalog;
    }

    public void registerProduct(final Context context, final Product product, final ProdRegListener appListener) {
        this.mContext = context;
        this.requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        final User mUser = new User(context);
        CacheHandler cacheHandler = getCacheHandler(context);
        cacheHandler.cacheProductsToRegister(product, mUser.getUserInstance(context));
        ArrayList<Product> products = cacheHandler.getProductsCached();

        if (!isUserSignedIn(mUser, context)) {
            appListener.onProdRegFailed(ErrorType.USER_NOT_SIGNED_IN);
        } else {
            if (!isValidaDate(product.getPurchaseDate())) {
                appListener.onProdRegFailed(ErrorType.INVALID_DATE);
            } else {
                UserProduct userProduct = getUserProduct();
                userProduct.setLocale(this.locale);
                userProduct.getRegisteredProducts(context, getRegisteredProductsListener(context, product, appListener));
            }
        }
    }

    @NonNull
    protected CacheHandler getCacheHandler(final Context context) {
        return new CacheHandler(context);
    }

    public void getRegisteredProducts(final Context context, final RegisteredProductsListener registeredProductsListener) {
        this.mContext = context;
        this.requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
        this.registeredProductsListener = registeredProductsListener;
        RegisteredProductsRequest registeredProductsRequest = getRegisteredProductsRequest(context);
        final RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(registeredProductsRequest, getPrxResponseListenerForRegisteredProducts(registeredProductsListener));
    }

    protected void handleError(final Product product, final int statusCode, final ProdRegListener appListener) {
//        new CacheHandler(mContext).deleteFile(product.getPath());
        if (statusCode == ErrorType.INVALID_CTN.getCode()) {
            appListener.onProdRegFailed(ErrorType.INVALID_CTN);
        } else if (statusCode == ErrorType.USER_TOKEN_EXPIRED.getCode()) {
            getUserProduct().onAccessTokenExpire(product, appListener);
        } else if (statusCode == ErrorType.ACCESS_TOKEN_INVALID.getCode()) {
            getUserProduct().onAccessTokenExpire(product, appListener);
        } else if (statusCode == ErrorType.INVALID_VALIDATION.getCode()) {
            appListener.onProdRegFailed(ErrorType.INVALID_VALIDATION);
        } else if (statusCode == ErrorType.INVALID_SERIALNUMBER.getCode()) {
            appListener.onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
        } else if (statusCode == ErrorType.NO_INTERNET_AVAILABLE.getCode()) {
            appListener.onProdRegFailed(ErrorType.NO_INTERNET_AVAILABLE);
        } else if (statusCode == ErrorType.INTERNAL_SERVER_ERROR.getCode()) {
            appListener.onProdRegFailed(ErrorType.INTERNAL_SERVER_ERROR);
        } else if (statusCode == ErrorType.METADATA_FAILED.getCode()) {
            appListener.onProdRegFailed(ErrorType.METADATA_FAILED);
        } else {
            appListener.onProdRegFailed(ErrorType.UNKNOWN);
        }
    }

    @NonNull
    protected RegisteredProductsRequest getRegisteredProductsRequest(final Context context) {
        RegisteredProductsRequest registeredProductsRequest = new RegisteredProductsRequest();
        registeredProductsRequest.setSector(getSector());
        registeredProductsRequest.setCatalog(getCatalog());
        registeredProductsRequest.setAccessToken(new User(context).getAccessToken());
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
                listener.onProdRegFailed(ErrorType.MISSING_DATE);
                return false;
            }
        } else
            product.setPurchaseDate(null);
        return true;
    }

    protected boolean isCtnRegistered(final RegisteredResponseData[] results, final Product product, final ProdRegListener listener) {
        for (RegisteredResponseData result : results) {
            if (product.getCtn().equalsIgnoreCase(result.getProductModelNumber())) {
                listener.onProdRegFailed(ErrorType.PRODUCT_ALREADY_REGISTERED);
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
        RegistrationRequest registrationRequest = new RegistrationRequest(product.getCtn(), product.getSerialNumber(), new User(context).getAccessToken());
        registrationRequest.setSector(product.getSector());
        registrationRequest.setCatalog(product.getCatalog());
        registrationRequest.setmLocale(product.getLocale());
        registrationRequest.setRegistrationChannel(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setPurchaseDate(product.getPurchaseDate());
        return registrationRequest;
    }

    protected void onAccessTokenExpire(final Product product, final ProdRegListener appListener) {
        final User user = new User(mContext);
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
                appListener.onProdRegFailed(ErrorType.REFRESH_ACCESS_TOKEN_FAILED);
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

    private boolean processSerialNumber(final ProductMetadataResponseData data, final Product product, final ProdRegListener listener) {
        if (product.getSerialNumber() == null || product.getSerialNumber().length() < 1) {
            listener.onProdRegFailed(ErrorType.MISSING_SERIALNUMBER);
            return true;
        } else if (!product.getSerialNumber().matches(data.getSerialNumberFormat())) {
            listener.onProdRegFailed(ErrorType.INVALID_SERIALNUMBER);
            return true;
        }
        return false;
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

}
