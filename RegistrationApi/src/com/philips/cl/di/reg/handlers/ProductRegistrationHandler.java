package com.philips.cl.di.reg.handlers;

public interface ProductRegistrationHandler {
  public void onRegisterSuccess(String response);
  public void onRegisterFailedWithFailure(int error);
}
