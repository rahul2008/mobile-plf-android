package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

/**
 * Created by Entreco on 15/12/2017.
 */

public interface ConsentHandlerInterface {

    void checkConsents(final ConsentListCallback callback);

    void post(final ConsentDefinition definition, boolean status, CreateConsentCallback callback);

//    func checkConsents(onConsentsChecked: @escaping (_ notGivenConsents: [Consent]?, _ outdatedConsents: [Consent]?, _ error: NSError?) -> Void)
//    func fetchLatestConsents(completion: @escaping (_ consents : [Consent]?, _ error: NSError?) -> Void)
//    func fetchLatestConsent(ofType type: String, completion: @escaping (_ consent : Consent?, _ error: NSError?) -> Void)
//    func fetchLatestConsentDefinitionStatus(completion: @escaping (_ consentStatusList : [ConsentDefinitionStatus]?, _ error: NSError?) -> Void)
//    func post(consentOfType consentType: String, withStatus status: Bool, withLocale locale: String, andVersion version: Int, completion : @escaping (_ success : Bool, _ error : NSError? ) -> Void)
}
