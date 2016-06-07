package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class UserWithProducts {

    public static final int PRODUCT_REGISTRATION = 0;
    public static final int FETCH_REGISTERED_PRODUCTS = 1;
    private final String TAG = getClass() + "";
    private int requestType = -1;
    private RegisteredProductsListener registeredProductsListener;
    private Context mContext;
    private User user;
    private LocalRegisteredProducts localRegisteredProducts;
    private ErrorHandler errorHandler;
    private String uuid = "";
    private ProdRegListener appListener;
    private RegisteredProduct currentRegisteredProduct;

    UserWithProducts(final Context context, final User user, final ProdRegListener appListener) {
        this.mContext = context;
        this.appListener = appListener;
        this.user = user;
        setUuid();
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
        currentRegisteredProduct = getUserProduct().createDummyRegisteredProduct(product);
        LocalRegisteredProducts localRegisteredProducts = getLocalRegisteredProductsInstance();
        final RegisteredProduct registeredProductIfExists = currentRegisteredProduct.getRegisteredProductIfExists(localRegisteredProducts);
        currentRegisteredProduct = registeredProductIfExists != null ? registeredProductIfExists : currentRegisteredProduct;
        if (currentRegisteredProduct.getRegistrationState() == RegistrationState.REGISTERED) {
            currentRegisteredProduct.setProdRegError(ProdRegError.PRODUCT_ALREADY_REGISTERED);
            appListener.onProdRegFailed(currentRegisteredProduct, getUserProduct());
        } else if (currentRegisteredProduct.getRegistrationState() != RegistrationState.REGISTERING) {
            localRegisteredProducts.store(currentRegisteredProduct);
            initRegistration(appListener, currentRegisteredProduct);
        }
    }

    /**
     * API to register products which are cached
     *
     * @param registeredProducts - List of products to be registered
     * @param appListener        - Call back listener
     */
    public void registerCachedProducts(final List<RegisteredProduct> registeredProducts, final ProdRegListener appListener) {
        for (RegisteredProduct registeredProduct : registeredProducts) {
            initRegistration(appListener, registeredProduct);
        }
    }

    protected void initRegistration(final ProdRegListener appListener, final RegisteredProduct registeredProduct) {
        final RegistrationState registrationState = registeredProduct.getRegistrationState();
        final boolean failedOnInvalidInput = isFailedOnInvalidInput(registeredProduct);
        if (!failedOnInvalidInput && (registrationState == RegistrationState.PENDING || registrationState == RegistrationState.FAILED) && getUuid().equals(registeredProduct.getUserUUid())) {
            Log.e(TAG, registeredProduct.getCtn() + "___" + registeredProduct.getSerialNumber() + "________" + registeredProduct.getUserUUid() + "_________" + getUuid());
            if (!getUserProduct().isUserSignedIn(mContext)) {
                getUserProduct().updateLocaleCache(registeredProduct, ProdRegError.USER_NOT_SIGNED_IN, RegistrationState.FAILED);
                appListener.onProdRegFailed(registeredProduct, getUserProduct());
            } else if (registeredProduct.getPurchaseDate() != null && registeredProduct.getPurchaseDate().length() != 0 && !getUserProduct().isValidDate(registeredProduct.getPurchaseDate())) {
                updateWithCallBack(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED, appListener);
            } else {
                UserWithProducts userWithProducts = getUserProduct();
                updateLocaleCache(registeredProduct, registeredProduct.getProdRegError(), RegistrationState.REGISTERING);
                userWithProducts.getRegisteredProducts(userWithProducts.getRegisteredProductsListener(registeredProduct, appListener));
            }
        }
    }

    protected boolean isFailedOnInvalidInput(final RegisteredProduct registeredProduct) {
        final ProdRegError prodRegError = registeredProduct.getProdRegError();
        return prodRegError != null && (prodRegError == ProdRegError.INVALID_CTN || prodRegError == ProdRegError.INVALID_SERIALNUMBER || prodRegError == ProdRegError.INVALID_SERIAL_NUMBER_AND_PURCHASE_DATE);
    }

    /**
     * API to fetch list of products which are registered locally and remote
     *
     * @param registeredProductsListener - call back listener to get list of products
     */
    public void getRegisteredProducts(final RegisteredProductsListener registeredProductsListener) {
        if (getUser().isUserSignIn()) {
            setRequestType(FETCH_REGISTERED_PRODUCTS);
            this.registeredProductsListener = registeredProductsListener;
            final RemoteRegisteredProducts remoteRegisteredProducts = new RemoteRegisteredProducts();
            remoteRegisteredProducts.getRegisteredProducts(mContext, getUserProduct(), getUser(), registeredProductsListener);
        } else {
            registeredProductsListener.getRegisteredProductsSuccess(getLocalRegisteredProductsInstance().getRegisteredProducts(), -1);
        }
    }

    /**
     * API will update to Error scenario to Locale cache
     *
     * @param registeredProduct - instance registeredProduct which include ctn,Sector and Catalog
     * @param prodRegError      - to get Error code and description
     * @param registrationState - registrationState as Enum's
     */
    public void updateLocaleCache(final RegisteredProduct registeredProduct, final ProdRegError prodRegError, final RegistrationState registrationState) {
        registeredProduct.setRegistrationState(registrationState);
        registeredProduct.setProdRegError(prodRegError);
        if (prodRegError == ProdRegError.INVALID_DATE || prodRegError == ProdRegError.MISSING_DATE) {
            getLocalRegisteredProductsInstance().removeProductFromCache(registeredProduct);
        } else
            getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
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
        return dates.length > 1 && Integer.parseInt(dates[0]) > 1999 && !isFutureDate(date);
    }

    protected boolean isFutureDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        final String mGetDeviceDate = dateFormat.format(calendar.getTime());
        try {
            final Date mDisplayDate = dateFormat.parse(date);
            final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
            return mDisplayDate.after(mDeviceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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
                if (!isCtnRegistered(registeredProducts, registeredProduct)) {
                    registeredProduct.getProductMetadata(mContext, getUserProduct().getMetadataListener(registeredProduct, appListener));
                } else {
                    updateWithCallBack(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.REGISTERED, appListener);
                }
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
                boolean isValidSerialNumber = isValidSerialNumber(productData, registeredProduct);
                boolean isValidDate = true;
                if (productData != null && productData.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
                    isValidDate = isValidPurchaseDate(registeredProduct.getPurchaseDate());
                }
                if (!isValidDate && !isValidSerialNumber) {
                    updateWithCallBack(registeredProduct, ProdRegError.INVALID_SERIAL_NUMBER_AND_PURCHASE_DATE, RegistrationState.FAILED, appListener);
                } else if (!isValidDate) {
                    updateWithCallBack(registeredProduct, ProdRegError.MISSING_DATE, RegistrationState.FAILED, appListener);
                } else if (!isValidSerialNumber) {
                    updateWithCallBack(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED, appListener);
                } else {
                    getUserProduct().makeRegistrationRequest(mContext, registeredProduct, appListener);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                getErrorHandler().handleError(getUserProduct(), registeredProduct, responseCode, appListener);
            }
        };
    }

    private void updateWithCallBack(final RegisteredProduct registeredProduct, final ProdRegError prodRegError, final RegistrationState registrationState, final ProdRegListener appListener) {
        getUserProduct().updateLocaleCache(registeredProduct, prodRegError, registrationState);
        appListener.onProdRegFailed(registeredProduct, getUserProduct());
        if (currentRegisteredProduct != null && currentRegisteredProduct.equals(registeredProduct)) {
            registerCachedProducts(getLocalRegisteredProductsInstance().getRegisteredProducts(), appListener);
        }
    }

    protected boolean isValidPurchaseDate(String purchaseDate) {
            return purchaseDate != null && purchaseDate.length() > 0;
    }

    protected boolean isCtnRegistered(final List<RegisteredProduct> registeredProducts, final RegisteredProduct registeredProduct) {
        for (RegisteredProduct result : registeredProducts) {
            if (registeredProduct.getCtn().equalsIgnoreCase(result.getCtn()) && registeredProduct.getSerialNumber().equals(result.getSerialNumber()) && result.getRegistrationState() == RegistrationState.REGISTERED) {
                return true;
            }
        }
        return false;
    }

    protected boolean isValidSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct) {
        if (data != null && data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            if (processSerialNumber(data, registeredProduct))
                return false;
        }
        return true;
    }

    private boolean processSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct registeredProduct) {
        final String serialNumber = registeredProduct.getSerialNumber();
        return serialNumber == null || serialNumber.length() < 1 || !serialNumber.matches(data.getSerialNumberFormat());
    }

    @NonNull
    protected RegistrationRequest getRegistrationRequest(final Context context, final RegisteredProduct registeredProduct) {
        RegistrationRequest registrationRequest = new RegistrationRequest(registeredProduct.getCtn(), registeredProduct.getSerialNumber(), getUser().getAccessToken());
        registrationRequest.setSector(registeredProduct.getSector());
        registrationRequest.setCatalog(registeredProduct.getCatalog());
        final String MICRO_SITE_ID = "MS";
        registrationRequest.setRegistrationChannel(MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        registrationRequest.setPurchaseDate(registeredProduct.getPurchaseDate());
        registrationRequest.setProductSerialNumber(registeredProduct.getSerialNumber());
        registrationRequest.setShouldSendEmailAfterRegistration(String.valueOf(registeredProduct.getEmail()));
        return registrationRequest;
    }

    /**
     * API refresh the access token
     *
     * @param registeredProduct - List of products to be registered
     * @param appListener       - Call back listener
     */
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
                if (registeredProduct != null) {
                    getLocalRegisteredProductsInstance().updateRegisteredProducts(registeredProduct);
                    updateWithCallBack(registeredProduct, ProdRegError.ACCESS_TOKEN_INVALID, RegistrationState.FAILED, appListener);
                }
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
                getUserProduct().getRegisteredProducts(getRegisteredProductsListener());
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
                if (currentRegisteredProduct != null && currentRegisteredProduct.equals(registeredProduct)) {
                    final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
                    getUserProduct().registerCachedProducts(registeredProducts, appListener);
                }
            }

            @Override
            public void onResponseError(PrxError prxError) {
                try {
                    getErrorHandler().handleError(getUserProduct(), registeredProduct, prxError.getStatusCode(), appListener);
                    if (currentRegisteredProduct.equals(registeredProduct)) {
                        final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
                        getUserProduct().registerCachedProducts(registeredProducts, appListener);
                    }
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
