/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;
import android.util.Log;


import com.ecs.demouapp.ui.address.AddressFields;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;

public class PaymentController {

    private Context mContext;
    private PaymentListener mPaymentListener;
    private MakePaymentListener mMakePaymentListener;
    private StoreListener mStore;

    public interface PaymentListener {
        void onGetPaymentDetails(Message msg);

        void onSetPaymentDetails(Message msg);
    }

    public interface MakePaymentListener {
        void onMakePayment(Message msg);

        void onPlaceOrder(Message msg);
    }

    public PaymentController(Context context, PaymentListener listener) {
        mContext = context;
        mPaymentListener = listener;
    }

    public PaymentController(Context context, MakePaymentListener listener) {
        mContext = context;
        mMakePaymentListener = listener;
    }

    public void getPaymentDetails() {

        ECSUtility.getInstance().getEcsServices().getPayments(new ECSCallback<PaymentMethods, Exception>() {
            @Override
            public void onResponse(PaymentMethods result) {


                Message message = new Message();
                if( result.getPayments()==null || result.getPayments().isEmpty()){
                    message.obj = "";
                }else{
                    message.obj = result;
                }
                mPaymentListener.onGetPaymentDetails(message);

            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj = error;
                mPaymentListener.onGetPaymentDetails(message);
            }
        });

        /*GetPaymentDetailRequest model = new GetPaymentDetailRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_PAYMENT_DETAILS, model, model);*/
    }

    public void setPaymentDetails(String paymentId) {
        /*HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, paymentId);*/

        ECSUtility.getInstance().getEcsServices().setPaymentMethod(paymentId, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                Message message = new Message();
                message.obj = result;
                mPaymentListener.onSetPaymentDetails(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj = error;
                mPaymentListener.onSetPaymentDetails(message);
            }
        });
        /*SetPaymentDetailsRequest model = new SetPaymentDetailsRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_PAYMENT_DETAILS, model, model);*/
    }

    public void placeOrder(String pSecurityCode) {
       /* final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        HashMap<String, String> query = new HashMap<>();
        if (pSecurityCode != null)
            query.put(ModelConstants.SECURITY_CODE, pSecurityCode);*/


        ////
        ECSUtility.getInstance().getEcsServices().submitOrder(pSecurityCode, new ECSCallback<OrderDetail, Exception>() {
            @Override
            public void onResponse(OrderDetail result) {
                Log.v("submitOrder","success");
                Message message = new Message();
                message.obj=result;
                mMakePaymentListener.onPlaceOrder(message);


            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj=detailErrorMessage;
                mMakePaymentListener.onPlaceOrder(message);

            }
        });

       /* PlaceOrderRequest request = new PlaceOrderRequest(delegate.getStore(), query, this);
        delegate.sendRequest(RequestCode.PLACE_ORDER, request, request);*/
    }

    public void makPayment(OrderDetail orderDetail) {

/*
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, orderID);

        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);


        PaymentRequest request = new PaymentRequest(delegate.getStore(), query, this);
        delegate.sendRequest(RequestCode.MAKE_PAYMENT, request, request);*/

        AddressFields address = CartModelContainer.getInstance().getBillingAddress();
       Addresses billingAddress = AddressController.getAddressesObject(address);
        billingAddress.setId(CartModelContainer.getInstance().getAddressId());
      //  params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
        //////////////////////////////

      ECSUtility.getInstance().getEcsServices().makePayment(orderDetail, billingAddress, new ECSCallback<MakePaymentData, Exception>() {
          @Override
          public void onResponse(MakePaymentData result) {
              Log.v("makePayment","success");
              Message message = new Message();
              message.obj=result;
              mMakePaymentListener.onMakePayment(message);
          }

          @Override
          public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
              Message message = new Message();
              message.obj=error;
              mMakePaymentListener.onMakePayment(message);
          }
      });

    }


    private void sendListener(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.GET_PAYMENT_DETAILS:
                mPaymentListener.onGetPaymentDetails(msg);
                break;
            case RequestCode.SET_PAYMENT_DETAILS:
                mPaymentListener.onSetPaymentDetails(msg);
                break;
            case RequestCode.PLACE_ORDER:
                mMakePaymentListener.onPlaceOrder(msg);
                break;
            case RequestCode.MAKE_PAYMENT:
                mMakePaymentListener.onMakePayment(msg);
                break;
        }
    }
}
