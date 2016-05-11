package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.error.ProdRegError;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponseData;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponse;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponseData;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
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
public class UserWithProducts {

    public static final int PRODUCT_REGISTRATION = 0;
    public static final int FETCH_REGISTERED_PRODUCTS = 1;
    private final String TAG = getClass() + "";
    private final String MICRO_SITE_ID = "MS";
    private int requestType = -1;
    private String locale;
    private RegisteredProductsListener registeredProductsListener;
    private Context mContext;
    private User user;
    private LocalRegisteredProducts localRegisteredProducts;
    private ErrorHandler errorHandler;
    private String uuid = "";
    private ProdRegListener appListener;
    private boolean requiresSerialNumber;
    private boolean requiresPurchaseDate;

    UserWithProducts(final Context context, final User user, final ProdRegListener appListener) {
        this.mContext = context;
        this.appListener = appListener;
        this.user = user;
        localRegisteredProducts = new LocalRegisteredProducts(context, this.user);
        errorHandler = new ErrorHandler();
    }

    /**
     * API get User UUID
     *
     * @return return user UUID
     */
    public String getUuid() {
        return uuid;
    }

    protected void setUuid() {
        this.uuid = getUser().getJanrainUUID() != null ? getUser().getJanrainUUID() : "";
    }

    /**
     * API to register product
     *
     * @param product - instance of product which should include CTN, Serial, Sector and Catalog of product
     */
    public void registerProduct(final Product product) {
        if (appListener == null) {
            throw new RuntimeException("Listener not Set");
        }
        setRequestType(PRODUCT_REGISTRATION);
        setUuid();
        RegisteredProduct registeredProduct = getUserProduct().createDummyRegisteredProduct(product);
        LocalRegisteredProducts localRegisteredProducts = getLocalRegisteredProductsInstance();
        if (!registeredProduct.IsUserRegisteredLocally(localRegisteredProducts)) {
            localRegisteredProducts.store(registeredProduct);
        } else {
            registeredProduct.setProdRegError(ProdRegError.PRODUCT_ALREADY_REGISTERED);
            appListener.onProdRegFailed(registeredProduct, getUserProduct());
        }

        final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
        getUserProduct().registerCachedProducts(registeredProducts, appListener);
    }

    /**
     * API to register products which are cached
     *
     * @param registeredProducts - List of products to be registered
     * @param appListener        - Call back listener
     */
    public void registerCachedProducts(final List<RegisteredProduct> registeredProducts, final ProdRegListener appListener) {
        for (RegisteredProduct registeredProduct : registeredProducts) {
            final RegistrationState registrationState = registeredProduct.getRegistrationState();
            if (registrationState == RegistrationState.PENDING || registrationState == RegistrationState.FAILED && getUuid().equals(registeredProduct.getUserUUid())) {
                Log.e(TAG, registeredProduct.getCtn() + "___" + registeredProduct.getSerialNumber() + "________" + registeredProduct.getUserUUid() + "_________" + getUuid());
                if (!getUserProduct().isUserSignedIn(mContext)) {
                    getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.USER_NOT_SIGNED_IN, RegistrationState.FAILED);
                    appListener.onProdRegFailed(registeredProduct, getUserProduct());
                } else if (registeredProduct.getPurchaseDate() != null && registeredProduct.getPurchaseDate().length() != 0 && !getUserProduct().isValidDate(registeredProduct.getPurchaseDate())) {
                    getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED);
                    appListener.onProdRegFailed(registeredProduct, getUserProduct());
                } else {
                    UserWithProducts userWithProducts = getUserProduct();
                    userWithProducts.setLocale(this.locale);
                    userWithProducts.getRegisteredProducts(userWithProducts.getRegisteredProductsListener(registeredProduct, appListener), registeredProduct.getSector(), registeredProduct.getCatalog());
                }
            }
        }
    }

    /**
     * API to fetch list of products which are registered locally and remote
     *
     * @param registeredProductsListener - call back listener to get list of products
     * @param sector
    //     * @param consumer
     */
    public void getRegisteredProducts(final RegisteredProductsListener registeredProductsListener, final Sector sector, final Catalog catalog) {
        if (getUser().isUserSignIn()) {
            setRequestType(FETCH_REGISTERED_PRODUCTS);
            this.registeredProductsListener = registeredProductsListener;
            final RemoteRegisteredProducts remoteRegisteredProducts = new RemoteRegisteredProducts();
            remoteRegisteredProducts.getRegisteredProducts(mContext, getUserProduct(), getUser(), registeredProductsListener, sector, catalog);
        } else {
            registeredProductsListener.getRegisteredProductsSuccess(getLocalRegisteredProductsInstance().getRegisteredProducts(), -1);
        }
    }

    public void updateLocaleCacheOnError(final RegisteredProduct registeredProduct, final ProdRegError prodRegError, final RegistrationState registrationState) {
        registeredProduct.setRegistrationState(registrationState);
        registeredProduct.setProdRegError(prodRegError);
        getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
    }

    private boolean processSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        if (registeredProduct.getSerialNumber() == null || registeredProduct.getSerialNumber().length() < 1) {
            getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(registeredProduct, getUserProduct());
            return true;
        } else if (!registeredProduct.getSerialNumber().matches(data.getSerialNumberFormat())) {
            getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            listener.onProdRegFailed(registeredProduct, getUserProduct());
            return true;
        }
        return false;
    }

    @NonNull
    protected RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    protected boolean isUserSignedIn(final Context context) {
        User mUser = getUser();
        return mUser.isUserSignIn() && mUser.getEmailVerificationStatus();
    }

    protected boolean isValidDate(final String date) {
        String[] dates = date.split("-");
        return dates.length > 1 && Integer.parseInt(dates[0]) > 1999;
    }

    @NonNull
    UserWithProducts getUserProduct() {
        return this;
    }

    int getRequestType() {
        return requestType;
    }

    protected void setRequestType(final int requestType) {
        this.requestType = requestType;
    }

    @NonNull
    RegisteredProductsListener getRegisteredProductsListener(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProductsSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                if (!isCtnRegistered(registeredProducts, registeredProduct, appListener))
                    registeredProduct.getProductMetadata(mContext, getUserProduct().getMetadataListener(registeredProduct, appListener));
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
                getErrorHandler().handleError(getUserProduct(), registeredProduct, responseCode, appListener);
            }
        };
    }

    protected boolean validatePurchaseDateFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener listener) {
        final String purchaseDate = registeredProduct.getPurchaseDate();
        if (data.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            requiresPurchaseDate = true;
            if (purchaseDate != null && purchaseDate.length() > 0) {
                return true;
            } else {
                getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.MISSING_DATE, RegistrationState.FAILED);
                listener.onProdRegFailed(registeredProduct, getUserProduct());
                return false;
            }
        } else
            requiresPurchaseDate = false;
        return true;
    }

    protected boolean isCtnRegistered(final List<RegisteredProduct> registeredProducts, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        for (RegisteredProduct result : registeredProducts) {
            if (registeredProduct.getCtn().equalsIgnoreCase(result.getCtn()) && registeredProduct.getSerialNumber().equals(result.getSerialNumber()) && result.getRegistrationState() == RegistrationState.REGISTERED) {
                getUserProduct().updateLocaleCacheOnError(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.REGISTERED);
                appListener.onProdRegFailed(registeredProduct, getUserProduct());
                return true;
            }
        }
        return false;
    }

    protected boolean validateSerialNumberFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        if (data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            requiresSerialNumber = true;
            if (processSerialNumber(data, registeredProduct, appListener))
                return false;
        } else {
            requiresSerialNumber = false;
        }
        return true;
    }

    @NonNull
    protected RegistrationRequest getRegistrationRequest(final Context context, final RegisteredProduct registeredProduct) {
        RegistrationRequest registrationRequest = new RegistrationRequest(registeredProduct.getCtn(), registeredProduct.getSerialNumber(), getUser().getAccessToken());
        registrationRequest.setRequiresSerialNumber(requiresSerialNumber);
        registrationRequest.setRequiresPurchaseDate(requiresPurchaseDate);
        registrationRequest.setSector(registeredProduct.getSector());
        registrationRequest.setCatalog(registeredProduct.getCatalog());
        registrationRequest.setRegistrationChannel(MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setPurchaseDate(registeredProduct.getPurchaseDate());
        registrationRequest.setProductSerialNumber(registeredProduct.getSerialNumber());
        registrationRequest.setShouldSendEmailAfterRegistration(registeredProduct.getEmail());
        return registrationRequest;
    }

    public void onAccessTokenExpire(final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        final User user = getUser();
        user.refreshLoginSession(getUserProduct().getRefreshLoginSessionHandler(registeredProduct, appListener, mContext));
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
                appListener.onProdRegFailed(registeredProduct, getUserProduct());
            }

            @Override
            public void onRefreshLoginSessionInProgress(final String s) {

            }
        };
    }

    protected void retryRequests(final Context mContext, final RegisteredProduct registeredProduct, final ProdRegListener appListener) {
        switch (requestType) {
            case PRODUCT_REGISTRATION:
                getUserProduct().makeRegistrationRequest(mContext, registeredProduct, appListener);
                break;
            case FETCH_REGISTERED_PRODUCTS:
                getUserProduct().getRegisteredProducts(getRegisteredProductsListener(), registeredProduct.getSector(), registeredProduct.getCatalog());
                break;
            default:
                break;
        }
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
                appListener.onProdRegSuccess(registeredProduct, getUserProduct());
            }

            @Override
            public void onResponseError(PrxError prxError) {
                try {
                    getErrorHandler().handleError(getUserProduct(), registeredProduct, prxError.getStatusCode(), appListener);
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
        setRequestType(PRODUCT_REGISTRATION);
        RegistrationRequest registrationRequest = getRegistrationRequest(mContext, registeredProduct);
        RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registrationRequest, getPrxResponseListener(registeredProduct, appListener));
    }

    protected RegisteredProductsListener getRegisteredProductsListener() {
        return registeredProductsListener;
    }

    /**
     * API return Locale
     *
     * @return return local as string
     */
    public String getLocale() {
        return locale;
    }

    /**
     * API set Local
     *
     * @param locale local
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    protected long getTimeStamp() {
        return System.currentTimeMillis();
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
            RegisteredProduct registeredProduct = new RegisteredProduct(product.getCtn(), product.getSector(), product.getCatalog());
            registeredProduct.setLocale(getLocale());
            registeredProduct.setSerialNumber(product.getSerialNumber());
            registeredProduct.setPurchaseDate(product.getPurchaseDate());
            registeredProduct.sendEmail(product.getEmail());
            registeredProduct.setRegistrationState(RegistrationState.PENDING);
            registeredProduct.setUserUUid(getUuid());
            return registeredProduct;
        }
        return null;
    }
}
