package com.philips.cl.di.reg.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;
import com.philips.cl.di.reg.errormapping.CheckLocale;

public class JanrainConfigurationSettings {
	
	private String REGISTRATION_VERSION = null; 
	
private String ProductEngageAppId = "ddjbpmgpeifijdlibdio";
private String ProductCaptureDomain = "philips.janraincapture.com";
private String ProductCaptureFlowVersion = "f21780f4-ef38-4d4b-8cef-2cf498642c0a";
private String ProductCaptureAppId = "hffxcm638rna8wrxxggx2gykhc";
private String ProductRegisterForgotEmailNative = "https://philips.janraincapture.com/oauth/forgot_password_native/";
private String ProductRegisterActivationUrl = "https://secure.philips.co.uk/myphilips/activateUser.jsp";
private String ProductRegisterForgotEmail = "https://secure.philips.co.uk/myphilips/resetPassword.jsp";

private String EvalEngageAppId = "jgehpoggnhbagolnihge";
private String EvalCaptureDomain = "philips.eval.janraincapture.com";
private String EvalCaptureFlowVersion = "f4a28763-840b-4a13-822a-48b80063a7bf";
private String EvalCaptureAppId = "nt5dqhp6uck5mcu57snuy8uk6c";
private String EvalRegisterForgotEmailNative = "https://philips.eval.janraincapture.com/oauth/forgot_password_native/";
private String EvalRegisterActivationUrl = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";
private String EvalRegisterForgotEmail = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

private String DeviceEngageAppId = "bdbppnbjfcibijknnfkk";
private String DeviceCaptureDomain = "philips.dev.janraincapture.com";
private String DeviceCaptureFlowVersion = "9549a1c4-575a-4042-9943-45b87a4f03f0";
private String DeviceCaptureAppId = "eupac7ugz25x8dwahvrbpmndf8";
private String DeviceRegisterForgotEmailNative = "https://philips.dev.janraincapture.com/oauth/forgot_password_native/";
private String DeviceRegisterActivationUrl = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";
private String DeviceRegisterForgotEmail = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

private String Register_Forgot_Email_Native;
private String Register_Activation_Url ;
private String Register_Forgot_Email;
private String CountryCode;
private String Language;

public void init(Context context,String CaptureClientId,String MicroSiteId,String registrationType) {
	
	SharedPreferences pref = context.getSharedPreferences("MyPref", 0); 
	Editor editor = pref.edit();
	editor.putString("microSiteId", MicroSiteId);
    editor.commit(); 
	
    REGISTRATION_VERSION = registrationType;
	
	JumpConfig jumpConfig = new JumpConfig();
	jumpConfig.captureClientId = CaptureClientId;
	jumpConfig.captureFlowName = "standard";
	jumpConfig.captureTraditionalRegistrationFormName = "registrationForm";
	jumpConfig.captureEnableThinRegistration = false;
	jumpConfig.captureSocialRegistrationFormName = "socialRegistrationForm";
	jumpConfig.captureForgotPasswordFormName = "forgotPasswordForm";
	jumpConfig.captureEditUserProfileFormName = "editProfileForm";
	jumpConfig.captureResendEmailVerificationFormName = "resendVerificationForm";
	jumpConfig.captureTraditionalSignInFormName = "userInformationForm";
	jumpConfig.traditionalSignInType = Jump.TraditionalSignInType.EMAIL;
	
	 //VARIABLES FOR VERSION CHECK TO BE SET IN APP 
    if(REGISTRATION_VERSION == "REGISTRATION_USE_PRODUCTION")
    {
			jumpConfig.engageAppId = ProductEngageAppId;
			jumpConfig.captureDomain = ProductCaptureDomain;
			jumpConfig.captureFlowVersion = ProductCaptureFlowVersion;
			jumpConfig.captureAppId = ProductCaptureAppId;
			Register_Forgot_Email_Native = ProductRegisterForgotEmailNative;
			Register_Activation_Url = ProductRegisterActivationUrl;
			Register_Forgot_Email = ProductRegisterForgotEmail;
    }
    else if(REGISTRATION_VERSION == "REGISTRATION_USE_EVAL")
    {
			jumpConfig.engageAppId = EvalEngageAppId;
			jumpConfig.captureDomain = EvalCaptureDomain;
			jumpConfig.captureFlowVersion = EvalCaptureFlowVersion;
			jumpConfig.captureAppId = EvalCaptureAppId;
			Register_Forgot_Email_Native = EvalRegisterForgotEmailNative;
			Register_Activation_Url = EvalRegisterActivationUrl;
			Register_Forgot_Email = EvalRegisterForgotEmail;
    }
    else if(REGISTRATION_VERSION == "REGISTRATION_USE_DEVICE")
    {
			jumpConfig.engageAppId = DeviceEngageAppId;
			jumpConfig.captureDomain = DeviceCaptureDomain;
			jumpConfig.captureFlowVersion = DeviceCaptureFlowVersion;
			jumpConfig.captureAppId = DeviceCaptureAppId;
			Register_Forgot_Email_Native = DeviceRegisterForgotEmailNative;
			Register_Activation_Url = DeviceRegisterActivationUrl;
			Register_Forgot_Email = DeviceRegisterForgotEmail;
    }
	 
    String LanguageCode = context.getResources().getConfiguration().locale.getLanguage();
    String CountryCode = context.getResources().getConfiguration().locale.getCountry(); 
	String locale =  LanguageCode+"-"+CountryCode;
    CheckLocale checkLocale = new CheckLocale();
    String localeLanguageCode = checkLocale.checkLanguage(locale);
    
    jumpConfig.captureLocale = localeLanguageCode;
    Jump.init(context, jumpConfig);   

}

}
