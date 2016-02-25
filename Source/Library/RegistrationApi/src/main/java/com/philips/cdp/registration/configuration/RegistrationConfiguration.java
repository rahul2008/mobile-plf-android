
package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationConfiguration {

    private boolean isCoppaFlow;

    private RegistrationFunction prioritisedFunction = RegistrationFunction.Registration;

    private static RegistrationConfiguration registrationConfiguration;

    private RegistrationConfiguration() {
    }

    public static RegistrationConfiguration getInstance() {
        if (registrationConfiguration == null) {
            registrationConfiguration = new RegistrationConfiguration();
        }
        return registrationConfiguration;
    }


    public JanRainConfiguration getJanRainConfiguration() {
        JanRainConfiguration dynJanRainConfiguration = RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration();
        if (null == dynJanRainConfiguration.getClientIds()) {
            return RegistrationStaticConfiguration.getInstance().getJanRainConfiguration();
        }

        JanRainConfiguration janRainConfiguration = new JanRainConfiguration();
        RegistrationClientId registrationClientId = new RegistrationClientId();

        if (null != RegistrationStaticConfiguration.getInstance().getJanRainConfiguration().getClientIds()) {
            registrationClientId = RegistrationStaticConfiguration.getInstance().getJanRainConfiguration().getClientIds();
        }


        if (null != dynJanRainConfiguration.getClientIds().getEvaluationId()) {
            registrationClientId.setEvaluationId(dynJanRainConfiguration.getClientIds().getEvaluationId());
        }
        if (null != dynJanRainConfiguration.getClientIds().getStagingId()) {
            registrationClientId.setStagingId(dynJanRainConfiguration.getClientIds().getStagingId());
        }
        if (null != dynJanRainConfiguration.getClientIds().getTestingId()) {
            registrationClientId.setTestingId(dynJanRainConfiguration.getClientIds().getTestingId());
        }
        if (null != dynJanRainConfiguration.getClientIds().getDevelopmentId()) {
            registrationClientId.setDevelopmentId(dynJanRainConfiguration.getClientIds().getDevelopmentId());
        }
        if (null != dynJanRainConfiguration.getClientIds().getProductionId()) {
            registrationClientId.setProductionId(dynJanRainConfiguration.getClientIds().getProductionId());
        }
        janRainConfiguration.setClientIds(registrationClientId);
        return janRainConfiguration;
    }


    public PILConfiguration getPilConfiguration() {
        PILConfiguration pilConfiguration = new PILConfiguration();

        if (null != RegistrationStaticConfiguration.getInstance().getPilConfiguration()) {
            pilConfiguration = RegistrationStaticConfiguration.getInstance().getPilConfiguration();
        }


        if (null != RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getCampaignID()) {
            pilConfiguration.setCampaignID(RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getCampaignID());
        }

        if (null != RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getMicrositeId()) {
            pilConfiguration.setMicrositeId(RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        }

        if (null != RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment()) {
            pilConfiguration.setRegistrationEnvironment(RegistrationDynamicConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        }


        return pilConfiguration;
    }

    public Flow getFlow() {

        if (RegistrationDynamicConfiguration.getInstance().getFlow() == null) {
            return RegistrationStaticConfiguration.getInstance().getFlow();
        }

        Flow flow = new Flow();

        if (null != RegistrationStaticConfiguration.getInstance().getFlow()) {
            flow = RegistrationStaticConfiguration.getInstance().getFlow();
        }

        Flow dynFlow = RegistrationDynamicConfiguration.getInstance().getFlow();

        if (null != dynFlow.isEmailVerificationRequired()) {
            flow.setEmailVerificationRequired(dynFlow.isEmailVerificationRequired());
        } else if (null == flow.isEmailVerificationRequired()) {
            flow.setEmailVerificationRequired(false);
        }

        if (null != dynFlow.isTermsAndConditionsAcceptanceRequired()) {
            flow.setTermsAndConditionsAcceptanceRequired(dynFlow.isTermsAndConditionsAcceptanceRequired());
        } else if (null == flow.isTermsAndConditionsAcceptanceRequired()) {
            flow.setTermsAndConditionsAcceptanceRequired(false);
        }
        if (null != dynFlow.getMinAgeLimit()) {
            if (null != flow.getMinAgeLimit()) {
                HashMap<String, String> temp = flow.getMinAgeLimit();
                temp.putAll(dynFlow.getMinAgeLimit());
                flow.setMinAgeLimit(temp);
            } else {
                flow.setMinAgeLimit(dynFlow.getMinAgeLimit());
            }
        }
        return flow;
    }

    public SigninProviders getSignInProviders() {

        HashMap<String, ArrayList<String>> providers = RegistrationDynamicConfiguration.getInstance().getSignInProviders().getProviders();

        RegUtility.checkIsValidSignInProviders(providers);

        if (RegistrationDynamicConfiguration.getInstance().getSignInProviders().getProviders() == null) {
            return RegistrationStaticConfiguration.getInstance().getSignInProviders();
        }

        HashMap<String, ArrayList<String>> temp = new HashMap<>();
        if (RegistrationStaticConfiguration.getInstance().getSignInProviders().getProviders() != null) {
            temp.putAll(RegistrationStaticConfiguration.getInstance().getSignInProviders().getProviders());
        }

        temp.putAll(RegistrationDynamicConfiguration.getInstance().getSignInProviders().getProviders());
        SigninProviders signinProviders = new SigninProviders();
        signinProviders.setProviders(temp);
        return signinProviders;
    }


    public HSDPConfiguration getHsdpConfiguration() {
        if (RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().getHsdpInfos().size() == 0) {
            return RegistrationStaticConfiguration.getInstance().getHsdpConfiguration();
        }


        HSDPConfiguration hsdpConfiguration = new HSDPConfiguration();
        HashMap<Configuration, HSDPInfo> hsdpClientInfos = new HashMap<>();
        if (RegistrationStaticConfiguration.getInstance().getHsdpConfiguration().getHsdpInfos().size() > 0) {
            hsdpClientInfos = RegistrationStaticConfiguration.getInstance().getHsdpConfiguration().getHsdpInfos();
        }

        hsdpClientInfos.putAll(RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().getHsdpInfos());
        hsdpConfiguration.setHsdpInfos(hsdpClientInfos);


        return hsdpConfiguration;
    }


    public boolean isCoppaFlow() {
        return isCoppaFlow;
    }

    public void setCoppaFlow(boolean isCoppaFlow) {
        this.isCoppaFlow = isCoppaFlow;
    }


    public void setPrioritisedFunction(RegistrationFunction prioritisedFunction) {
        this.prioritisedFunction = prioritisedFunction;
    }

    public RegistrationFunction getPrioritisedFunction() {
        return prioritisedFunction;
    }


}
