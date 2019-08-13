package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * This class is used to provide input parameters and customizations for USR.
 * @since 1.0.0
 */

public class URLaunchInput extends UappLaunchInput {

    private RegistrationLaunchMode registrationLaunchMode = RegistrationLaunchMode.MARKETING_OPT;

    private RegistrationContentConfiguration registrationContentConfiguration;

    private static final long serialVersionUID = 1128016096756071382L;


    /**
     * Get status of is current fragment need to add to back stack or not.
     *
     * @return true if need to add to fragment back stack
     * @since 1.0.0
     */
    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }


    /**
     * Enable add to back stack for current fragment.
     *
     * @param isAddToBackStack pass true to save User Registration screens to back stack or else false
     * @since 1.0.0
     */
    public void enableAddtoBackStack(boolean isAddToBackStack) {
        this.isAddToBackStack = isAddToBackStack;
    }

    private boolean isAddToBackStack;

    private RegistrationFunction registrationFunction;

    private UserRegistrationUIEventListener userRegistrationListener;

    public ConsentStates getUserPersonalConsentStatus() {
        return userPersonalConsentStatus;
    }

    public void setUserPersonalConsentStatus(ConsentStates userPersonalConsentStatus) {
        this.userPersonalConsentStatus = userPersonalConsentStatus;
    }

    private ConsentStates userPersonalConsentStatus;
    /**
     * Get Registration function.
     * @return RegistrationFunction  instance of RegistrationFunction
     * @since 1.0.0
     */
    public RegistrationFunction getRegistrationFunction() {
        return registrationFunction;
    }

    /**
     * RegistrationFunction is used to prioritize  between Create account and Sign in.
     * RegistrationFunction.Registration  Will display the Create account option on top
     * RegistrationFunction.SignIn  Will display the Sign in option on top.
     *
     * @param registrationFunction instance of RegistrationFunction
     * @since 1.0.0
     */
    public void setRegistrationFunction(RegistrationFunction registrationFunction) {
        this.registrationFunction = registrationFunction;
    }

    /**
     * Set a UserRegistrationUIEventListener to provide custom implementations of
     * Terms and conditions, Privacy policy and know about user registration completion.
     *
     * @param userRegistrationListener  instance of UserRegistrationUIEventListener
     * @since 1.0.0
     */
    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationListener) {
        this.userRegistrationListener = userRegistrationListener;
    }

    /**
     * API returns UserRegistrationUIEventListener instance .
     * @return UserRegistrationUIEventListener
     * @since 1.0.0
     */
    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return this.userRegistrationListener;
    }

    /**
     * API returns RegistrationLaunchMode instance
     * @return RegistrationLaunchMode
     * @since 1.0.0
     */
    public RegistrationLaunchMode getEndPointScreen() {
        return registrationLaunchMode;
    }

    /**
     * API sets the end point screen from RegistrationLaunchMode
     * @param registrationLaunchMode
     */
    public void setEndPointScreen(RegistrationLaunchMode registrationLaunchMode) {
        this.registrationLaunchMode = registrationLaunchMode;
    }

    /**
     * Used to set custom content on the marketing opt in page and home page.
     * Please see RegistrationContentConfiguration class for more details.
     *
     * @param registrationContentConfiguration  RegistrationContentConfiguration registrationContentConfiguration
     * @since 1.0.0
     */
    public void setRegistrationContentConfiguration(RegistrationContentConfiguration registrationContentConfiguration) {
        this.registrationContentConfiguration = registrationContentConfiguration;
    }

    /**
     * API returns RegistrationContentConfiguration
     * @return RegistrationContentConfiguration
     * @since 1.0.0
     */
    public RegistrationContentConfiguration getRegistrationContentConfiguration() {
        return this.registrationContentConfiguration;
    }

    UIFlow uiFlow;

    /**
     * Used to override the UI flow. Setting this will disable any server side A/B testing.
     * Advised not to use for normal use-cases.
     *
     * @param uiFlow  Any one of the UIFlow enum values.
     *               @since 1.0.0
     */
    public void setUIFlow(UIFlow uiFlow) {
        this.uiFlow = uiFlow;
    }

    /**
     * API returns UIFlow .
     * @return UIFlow
     * @since 1.0.0
     */
    public UIFlow getUIflow() {
        return uiFlow;
    }


}
