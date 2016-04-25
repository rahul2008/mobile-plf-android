package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.handler.MetadataListener;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.handler.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegisteredResponse;
import com.philips.cdp.prodreg.model.RegisteredResponseData;
import com.philips.cdp.prodreg.model.RegistrationResponse;
import com.philips.cdp.prodreg.model.RegistrationResponseData;
import com.philips.cdp.prodreg.model.RegistrationState;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProduct {

    public static final String PRODUCT_REGISTRATION = "product_registration";
    public static final String FETCH_REGISTERED_PRODUCTS = "fetch_registered_products";
    private final String TAG = getClass() + "";
    private final String MICRO_SITE_ID = "MS";
    private String requestType;
    private String locale;
    private RegisteredProductsListener registeredProductsListener;
    private Context mContext;
    private User user;
    private LocalRegisteredProducts localRegisteredProducts;
    private ErrorHandler errorHandler;
    private String uuid = "";

    public UserProduct(final Context context) {
        this.mContext = context;
        user = new User(context);
        localRegisteredProducts = new LocalRegisteredProducts(context, user);
        errorHandler = new ErrorHandler(context);
    }

    public String getUuid() {
        return uuid;
    }

    protected void setUuid() {
        final DIUserProfile userInstance = getUser().getUserInstance(mContext);
        this.uuid = userInstance != null ? userInstance.getJanrainUUID() : "";
    }

    public void registerProduct(final Product product, final ProdRegListener appListener) {
        setRequestType(PRODUCT_REGISTRATION);
        setUuid();
        RegisteredProduct registeredProduct = getUserProduct().createDummyRegisteredProduct(product);
        LocalRegisteredProducts localRegisteredProducts = getLocalRegisteredProductsInstance();
        if (!getUserProduct().IsUserRegisteredLocally(registeredProduct)) {
            localRegisteredProducts.store(registeredProduct);
        } else {
            registeredProduct.setProdRegError(ProdRegError.PRODUCT_ALREADY_REGISTERED);
            appListener.onProdRegFailed(registeredProduct);
        }

        final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
        getUserProduct().registerCachedProducts(registeredProducts, appListener);
    }

    protected boolean IsUserRegisteredLocally(final RegisteredProduct registeredProduct) {
        final List<RegisteredProduct> registeredProducts = getLocalRegisteredProductsInstance().getRegisteredProducts();
        final int index = registeredProducts.indexOf(registeredProduct);
        if (index != -1) {
            RegisteredProduct product = registeredProducts.get(index);
            final RegistrationState registrationState = product.getRegistrationState();
            return registrationState == RegistrationState.REGISTERED;
        }
        return false;
    }

    @NonNull
    protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
        return localRegisteredProducts;
    }

    @NonNull
    protected User getUser() {
        return user;
    }

    protected RegisteredProduct createDummyRegisteredProduct(final Product product) {
        if (product != null) {
            RegisteredProduct registeredProduct = new RegisteredProduct(product.getCtn(), product.getSerialNumber(), product.getPurchaseDate(), product.getSector(), product.getCatalog());
            registeredProduct.setLocale(product.getLocale());
            registeredProduct.setShouldSendEmailAfterRegistration(product.getShouldSendEmailAfterRegistration());
            registeredProduct.setRegistrationState(RegistrationState.PENDING);
            registeredProduct.setUserUUid(getUuid());
            return registeredProduct;
        }
        return null;
    }

    public void registerCachedProducts(final List<RegisteredProduct> registeredProducts, final ProdRegListener appListener) {
        for (RegisteredProduct registeredProduct : registeredProducts) {
            final RegistrationState registrationState = registeredProduct.getRegistrationState();
            if (registrationState == RegistrationState.PENDING || registrationState == RegistrationState.FAILED && getUuid().equals(registeredProduct.getUserUUid())) {
                Log.e(TAG, registeredProduct.getCtn() + "___" + registeredProduct.getSerialNumber() + "________" + registeredProduct.getUserUUid() + "_________" + getUuid());
                if (!getUserProduct().isUserSignedIn(mContext)) {
                    // TO-DO whether required to change state
                    getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.USER_NOT_SIGNED_IN, RegistrationState.FAILED);
                    getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
                    appListener.onProdRegFailed(registeredProduct);
                } else if (registeredProduct.getPurchaseDate() != null && registeredProduct.getPurchaseDate().length() != 0 && !getUserProduct().isValidDate(registeredProduct.getPurchaseDate())) {
                    getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED);
                    getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
                    appListener.onProdRegFailed(registeredProduct);
                } else {
                    UserProduct userProduct = getUserProduct();
                    userProduct.setLocale(this.locale);
                    userProduct.getRegisteredProducts(userProduct.getRegisteredProductsListener(registeredProduct, appListener));
                }
            }
        }
    }

    public void getRegisteredProducts(final RegisteredProductsListener registeredProductsListener) {
        setRequestType(FETCH_REGISTERED_PRODUCTS);
        this.registeredProductsListener = registeredProductsListener;
        RegisteredProductsRequest registeredProductsRequest = getRegisteredProductsRequest();
        final RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registeredProductsRequest, getPrxResponseListenerForRegisteredProducts(registeredProductsListener));
    }

    protected void updateLocaleCacheOnError(final RegisteredProduct registeredProduct, final ProdRegError prodRegError, final RegistrationState registrationState) {
        registeredProduct.setRegistrationState(registrationState);
        registeredProduct.setProdRegError(prodRegError);
        getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
    }

    @NonNull
    protected RegisteredProductsRequest getRegisteredProductsRequest() {
        RegisteredProductsRequest registeredProductsRequest = new RegisteredProductsRequest();
        registeredProductsRequest.setAccessToken(getUser().getAccessToken());
        return registeredProductsRequest;
    }

    @NonNull
    protected RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    protected boolean isUserSignedIn(final Context context) {
        User mUser = getUser();
        return mUser.isUserSignIn(context) && mUser.getEmailVerificationStatus(context);
    }

    protected boolean isValidDate(final String date) {
        String[] dates = date.split("-");
        return dates.length > 1 && Integer.parseInt(dates[0]) > 1999;
    }

    @NonNull
    UserProduct getUserProduct() {
        return this;
    }

    String getRequestType() {
        return requestType;
    }

    protected void setRequestType(final String requestType) {
        this.requestType = requestType;
    }

    @NonNull
    RegisteredProductsListener getRegisteredProductsListener(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProducts(final RegisteredResponse registeredDataResponse) {
                RegisteredResponseData[] results = registeredDataResponse.getResults();
                final LocalRegisteredProducts localRegisteredProductsInstance = getLocalRegisteredProductsInstance();
                Gson gson = getGson();
                RegisteredProduct[] registeredProducts = getUserProduct().getRegisteredProductsFromResponse(results, gson);
                localRegisteredProductsInstance.syncLocalCache(registeredProducts);

                if (!isCtnRegistered(results, registeredProduct, appListener))
                    registeredProduct.getProductMetadata(mContext, getMetadataListener(registeredProduct, appListener));
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getErrorHandler().handleError(registeredProduct, responseCode, appListener);
            }
        };
    }

    protected ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected RegisteredProduct[] getRegisteredProductsFromResponse(final RegisteredResponseData[] results, final Gson gson) {
        return gson.fromJson(gson.toJson(results), RegisteredProduct[].class);
    }

    @NonNull
    protected Gson getGson() {
        return new Gson();
    }

    @NonNull
    MetadataListener getMetadataListener(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
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
                getErrorHandler().handleError(registeredProduct, responseCode, appListener);
            }
        };
    }

    protected boolean validatePurchaseDateFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        final String purchaseDate = registeredProduct.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_DATE, RegistrationState.FAILED);
                listener.onProdRegFailed(registeredProduct);
                return false;
            }
        } else
            registeredProduct.setPurchaseDate(null);
        return true;
    }

    protected boolean isCtnRegistered(final RegisteredResponseData[] results, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        for (RegisteredResponseData result : results) {
            if (registeredProduct.getCtn().equalsIgnoreCase(result.getProductModelNumber())) {
                getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.REGISTERED);
                appListener.onProdRegFailed(registeredProduct);
                return true;
            }
        }
        return false;
    }

    protected boolean validateSerialNumberFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, registeredProduct, appListener)) return false;
        } else {
            registeredProduct.setSerialNumber("");
        }
        return true;
    }

    @NonNull
    protected RegistrationRequest getRegistrationRequest(final Context context, final RegisteredProduct registeredProduct) {
        RegistrationRequest registrationRequest = new RegistrationRequest(registeredProduct.getCtn(), registeredProduct.getSerialNumber(), getUser().getAccessToken());
        registrationRequest.setSector(registeredProduct.getSector());
        registrationRequest.setCatalog(registeredProduct.getCatalog());
        registrationRequest.setmLocale(registeredProduct.getLocale());
        registrationRequest.setRegistrationChannel(MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setPurchaseDate(registeredProduct.getPurchaseDate());
        return registrationRequest;
    }

    protected void onAccessTokenExpire(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        final User user = getUser();
        user.refreshLoginSession(getUserProduct().getRefreshLoginSessionHandler(registeredProduct, appListener, mContext), mContext);
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
                getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.ACCESS_TOKEN_INVALID, RegistrationState.FAILED);
                getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
                appListener.onProdRegFailed(registeredProduct);
            }
        };
    }

    protected void retryRequests(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        switch (requestType) {
            case PRODUCT_REGISTRATION:
                getUserProduct().makeRegistrationRequest(mContext, registeredProduct, appListener);
                break;
            case FETCH_REGISTERED_PRODUCTS:
                getUserProduct().getRegisteredProducts(getRegisteredProductsListener());
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
                RegistrationResponse registrationResponse = (RegistrationResponse) responseData;
                getUserProduct().mapRegistrationResponse(registrationResponse, registeredProduct);
                registeredProduct.setProdRegError(null);
                getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
                appListener.onProdRegSuccess(registeredProduct);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                try {
                    getErrorHandler().handleError(registeredProduct, responseCode, appListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    protected void mapRegistrationResponse(final RegistrationResponse registrationResponse, final RegisteredProduct registeredProduct) {
        final RegistrationResponseData data = registrationResponse.getData();
        registeredProduct.setEndWarrantyDate(data.getWarrantyEndDate());
        registeredProduct.setContractNumber(data.getContractNumber());
    }

    protected void makeRegistrationRequest(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        this.requestType = PRODUCT_REGISTRATION;
        RegistrationRequest registrationRequest = getRegistrationRequest(mContext, registeredProduct);
        registrationRequest.setRegistrationChannel(MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setmLocale(registeredProduct.getLocale());
        registrationRequest.setPurchaseDate(registeredProduct.getPurchaseDate());
        registrationRequest.setProductSerialNumber(registeredProduct.getSerialNumber());
        registrationRequest.setShouldSendEmailAfterRegistration(registeredProduct.getShouldSendEmailAfterRegistration());
        RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registrationRequest, getPrxResponseListener(registeredProduct, appListener));
    }

    public RegisteredProductsListener getRegisteredProductsListener() {
        return registeredProductsListener;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    private boolean processSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        if (registeredProduct.getSerialNumber() == null || registeredProduct.getSerialNumber().length() < 1) {
            getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(registeredProduct);
            return true;
        } else if (!registeredProduct.getSerialNumber().matches(data.getSerialNumberFormat())) {
            getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(registeredProduct);
            return true;
        }
        return false;
    }
}
