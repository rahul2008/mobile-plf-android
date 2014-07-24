package com.philips.cl.di.dev.pa.cpp;

import java.net.HttpURLConnection;
import java.util.Date;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.PermissionListener;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.TaskPutDeviceDetails;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;
import com.philips.icpinterface.data.PairingInfo;
import com.philips.icpinterface.data.PairingReceivedRelationships;
import com.philips.icpinterface.data.PairingReceivedRelationships.PairingEntity;
import com.philips.icpinterface.data.PairingRelationship;

/**
 */
public class PairingHandler implements ICPEventListener, ServerResponseListener {

	private ICPCallbackHandler callbackHandler;
	private String currentRelationshipType = null;
	private PairingListener pairingListener;
	private PurifierDatabase purifierDatabase;
	private String secretKey;

	private PurAirDevice purifier;
	private PermissionListener permissionListener=null;
	private CPPController cppController ;
	private enum ENTITY {PURIFIER, APP};
	private ENTITY entity_state;

	/**
	 * Constructor for Pairinghandler.
	 * 
	 * @param context
	 *            Context
	 * @param iPairingListener
	 *            PairingListener
	 * @param purifierEui64
	 *            String
	 */
	public PairingHandler(PairingListener iPairingListener,	PurAirDevice purifier) {
		this.purifier = purifier;
		purifierDatabase = new PurifierDatabase();
		pairingListener = iPairingListener;
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
		cppController = CPPController.getInstance(PurAirApplication.getAppContext()) ;
	}

	public void setPermissionListener(PermissionListener iPermissionListener) {
		permissionListener = iPermissionListener;
	}

	/**
	 * Method startPairing- starts pairing procedure
	 * 
	 * @param relationshipType
	 *            String
	 * @param permission
	 *            String[]
	 */
	public void startPairing() {
		ALog.i(ALog.PAIRING, "Started pairing with purifier - eui64= " + purifier.getEui64());
		currentRelationshipType = AppConstants.PAIRING_DI_COMM_RELATIONSHIP;
		getRelationship(currentRelationshipType, purifier.getEui64());
	}

	/**
	 * Method getRelationship- fetches existing relationships
	 * 
	 * @param relationshipType
	 *            String

	 * @param purifierEui64
	 *            String
	 */
	private void getRelationship(String relationshipType, String purifierEui64) {
		ALog.i(ALog.PAIRING, "Requesting existing relationships");

		if(!cppController.isSignOn())return;

		ALog.i(ALog.PAIRING, "signOn is success");
		boolean bincludeIncoming = true;
		boolean bincludeOutgoing = true;
		int iMetadataSize = 0;
		int iMaxPermissions = 10;
		int iMaxRelations = 5;
		int iRelOffset = 0;
		int retValue = 0;
		PairingService getRelations = new PairingService(callbackHandler);

		getRelations
		.setPairingServiceCommand(Commands.PAIRING_GET_RELATIONSHIPS);
		retValue = getRelations.getRelationshipRequest(getPurifierEntity(),
				relationshipType, bincludeIncoming, bincludeOutgoing,
				iMetadataSize, iMaxPermissions, iMaxRelations, iRelOffset);
		if (Errors.SUCCESS != retValue) {
			ALog.d(ALog.PAIRING, "Check Parameters: " + retValue);
			return;
		}
		retValue = getRelations.executeCommand();
		if (Errors.SUCCESS != retValue) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retValue);
		}
	}

	/**
	 * Method startPairingPortTask.
	 * 
	 * @param relationshipType
	 *            String
	 * @param permission
	 *            String[]
	 * @param context
	 *            Context
	 */
	private void startPairingPortTask(final String relationshipType,
			final String[] permission) {

		if (relationshipType.equals(AppConstants.PAIRING_DI_COMM_RELATIONSHIP)) {
			secretKey = generateRandomSecretKey();
			String pairing_url = Utils.getPortUrl(Port.PAIRING, purifier.getIpAddress());
			String appEui64 = SessionDto.getInstance().getAppEui64();
			String dataToUpload = JSONBuilder.getDICOMMPairingJSON(appEui64, secretKey);
			dataToUpload = new DISecurity(null).encryptData(dataToUpload, purifier);
			TaskPutDeviceDetails pairingRunnable = new TaskPutDeviceDetails(
					dataToUpload, pairing_url, this);
			Thread pairingThread = new Thread(pairingRunnable);
			pairingThread.start();

		} else {
			currentRelationshipType = relationshipType;
			addRelationship(relationshipType, null);
		}
	}

	/**
	 * Method addRelationship.
	 * 
	 * @param relationshipType
	 *            String
	 * @param permission
	 *            String[]
	 * @param secretKey
	 *            String
	 */
	private void addRelationship(String relationshipType, String secretKey) {

		if(!cppController.isSignOn())return;
		int status;
		PairingService addPSRelation = new PairingService(callbackHandler);

		if (secretKey != null && relationshipType.equals(AppConstants.PAIRING_DI_COMM_RELATIONSHIP)) {
			addPSRelation.addRelationshipRequest(null, getPurifierEntity(),
					null, getPairingRelationshipData(relationshipType, AppConstants.PAIRING_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PERMISSIONS.size()])), getPairingInfo(secretKey));
		} else {
			addPSRelation.addRelationshipRequest(null, getPurifierEntity(),
					null, getPairingRelationshipData(relationshipType, AppConstants.PAIRING_PUSH_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PUSH_PERMISSIONS.size()])), null);
		}

		addPSRelation.setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
		status = addPSRelation.executeCommand();
		if (Errors.SUCCESS != status) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: ");
		}

	}

	public void initializeRelationshipRemoval(){
		currentRelationshipType = AppConstants.PAIRING_DI_COMM_RELATIONSHIP;
		entity_state= ENTITY.PURIFIER;
		removeRelationship(null, getPurifierEntity(), currentRelationshipType);
	}

	/**
	 * Method removeRelationship-remove an existing relationship
	 * @param relationType String
	 */
	private void removeRelationship(PairingEntitiyReference trustor, PairingEntitiyReference trustee,  String relationType){
		if(!cppController.isSignOn()) return;
		PairingService removeRelationship = new PairingService(callbackHandler);
		int retStatus;

		retStatus = removeRelationship.removeRelationshipRequest(trustor, trustee, relationType);
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);
			return;
		}
		removeRelationship.setPairingServiceCommand(Commands.PAIRING_REMOVE_RELATIONSHIP);
		retStatus = removeRelationship.executeCommand();
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);			
		}
	}


	/**
	 * add pairingRelationshipData
	 * 
	 * @param permission
	 * @param relationshipType
	 * @return 
	 */
	private PairingRelationship getPairingRelationshipData(String relationshipType,
			String[] permission) {
		PairingRelationship pairingRelationshipData = new PairingRelationship();
		pairingRelationshipData.pairingRelationshipIsAllowDelegation = true;
		pairingRelationshipData.pairingRelationshipMetadata = null;
		pairingRelationshipData.pairingRelationshipRelationType = relationshipType;
		pairingRelationshipData.pairingRelationshipTTL = AppConstants.PAIRING_RELATIONSHIPDURATION_SEC;
		pairingRelationshipData.pairingRelationshipPermissionArray = permission;
		return pairingRelationshipData;
	}

	/**
	 * add Trustee data
	 * 
	 * @param purifierEui64
	 * @param pairingTrustee
	 * 
	 * @return PairingEntitiyReference */
	private PairingEntitiyReference getPurifierEntity() {
		PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();	
		pairingTrustee.entityRefId = purifier.getEui64();
		pairingTrustee.entityRefProvider = AppConstants.PAIRING_REFERENCEPROVIDER;
		pairingTrustee.entityRefType = AppConstants.PAIRING_REFERENCETYPE;
		pairingTrustee.entityRefCredentials = null;

		ALog.i(ALog.PAIRING, "purifier entityRefId" + pairingTrustee.entityRefId);
		ALog.i(ALog.PAIRING, "purifier entityRefType" + pairingTrustee.entityRefType);

		return pairingTrustee;
	}

	/**
	 * add Trustee data
	 * 
	 * @param purifierEui64
	 * @param pairingTrustee
	 * 
	 * @return PairingEntitiyReference */
	private PairingEntitiyReference getAppEntity() {
		PairingEntitiyReference pairingTrustor = new PairingEntitiyReference();	
		pairingTrustor.entityRefId = SessionDto.getInstance().getAppEui64();
		pairingTrustor.entityRefProvider = AppConstants.PAIRING_REFERENCEPROVIDER;
		pairingTrustor.entityRefType = AppConstants.PAIRING_APP_REFERENCETYPE;
		pairingTrustor.entityRefCredentials = null;

		ALog.i(ALog.PAIRING, "app entityRefId" + pairingTrustor.entityRefId);
		ALog.i(ALog.PAIRING, "app entityRefType" + pairingTrustor.entityRefType);

		return pairingTrustor;
	}

	/**
	 * add pairing info
	 * 
	 * @param secretKey
	 * @return 
	 */
	private PairingInfo getPairingInfo(String secretKey) {
		PairingInfo pairingTypeInfo = new PairingInfo();
		pairingTypeInfo.pairingInfoIsMatchIPAddr = false;
		pairingTypeInfo.pairingInfoRequestTTL = AppConstants.PAIRING_REQUESTTTL_MIN;
		pairingTypeInfo.pairingInfoSecretKey = secretKey;
		return pairingTypeInfo;
	}

	/**
	 * generates random key
	 * 
	 * @return random secret key */
	public String generateRandomSecretKey() {		
		return Long.toHexString(Double.doubleToLongBits(Math.random()));

	}

	/**
	 * Method onICPCallbackEventOccurred.
	 * 
	 * @param eventType
	 *            int
	 * @param status
	 *            int
	 * @param obj
	 *            ICPClient

	 * @see com.philips.cl.di.dev.pa.cpp.ICPEventListener#onICPCallbackEventOccurred(int,
	 *      int, ICPClient) */
	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		ALog.d(ALog.PAIRING, "onICPCallbackEventOccurred eventType " + eventType + " status " + status);

		if (status != Errors.SUCCESS) {
			if(permissionListener==null){
				ALog.e(ALog.PAIRING, "Pairing call-FAILED (get or add)");
				notifyListenerFailed();
			}else{
				ALog.e(ALog.PAIRING, "get permission call failed");
				permissionListener.onCallFailed();
			}
			return;
		}

		PairingService pairingObj = (PairingService) obj;
		int noOfRelations= getNumberOfRelationships(pairingObj);
		int diCommRelationships = pairingObj.getNumberOfRelationsReturned();
		if (eventType == Commands.PAIRING_GET_RELATIONSHIPS) {			
			ALog.i(ALog.PAIRING, "GetRelation call-SUCCESS");
			if (diCommRelationships < 1 || noOfRelations<1) {
				ALog.i(ALog.PAIRING, "No existing relationships - Requesting Purifier to start pairing");
				startPairingPortTask(currentRelationshipType, AppConstants.PAIRING_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PERMISSIONS.size()]));
			} 
			else if (diCommRelationships < 2 || noOfRelations<1) {
				ALog.i(ALog.PAIRING, "Only one existing relationship (one expired) - Need to start pairing again");
				startPairingPortTask(currentRelationshipType, AppConstants.PAIRING_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PERMISSIONS.size()]));
			} 
			else if (currentRelationshipType.equals(AppConstants.PAIRING_DI_COMM_RELATIONSHIP) && noOfRelations>0) {
				currentRelationshipType = AppConstants.PAIRING_NOTIFY_RELATIONSHIP;
				getRelationship(currentRelationshipType, purifier.getEui64());
				ALog.i(ALog.PAIRING, "DI COMM relationship exists, checking for notify relationship");
			} 
			else if (currentRelationshipType.equals(AppConstants.PAIRING_NOTIFY_RELATIONSHIP) && noOfRelations>0) {
				ALog.i(ALog.PAIRING, "Notify relationship exists, pairing already successfull");
				notifyListenerSuccess();
				ALog.i(ALog.PAIRING, "Paring status set to true");
				purifier.setPairing(PurAirDevice.PAIRED_STATUS.PAIRED);
				purifier.setLastPairedTime(new Date().getTime()) ;
				purifierDatabase.updatePairingStatus(purifier, PurAirDevice.PAIRED_STATUS.PAIRED);
				/*if( updated != -1 ) {
					String currentPurifierEui64 = PurifierManager.getInstance().getDefaultPurifierEUI64();
					PurAirDevice firstPurifier = DiscoveryManager.getInstance().getDeviceByEui64(currentPurifierEui64);
					if (firstPurifier == null) return;
					PurifierManager.getInstance().setCurrentPurifier(purifier) ;
				}*/
			}
		} 

		else if(eventType == Commands.PAIRING_REMOVE_RELATIONSHIP){
			ALog.i(ALog.PAIRING, "RemoveRelation call-SUCCESS");
			if (currentRelationshipType.equals(AppConstants.PAIRING_DI_COMM_RELATIONSHIP)){
				if (entity_state== ENTITY.PURIFIER) {
					ALog.i(ALog.PAIRING, "Outgoing di-comm relationship (one removed) - Need to remove the other");
					entity_state= ENTITY.APP;
					new Thread(new Runnable() {					
						@Override
						public void run() {
							removeRelationship(getPurifierEntity(), getAppEntity(), currentRelationshipType);
						}
					}).start();

				} else if (entity_state== ENTITY.APP) {
					ALog.i(ALog.PAIRING, "DI-COMM Relationship removed successfully");
					entity_state= ENTITY.PURIFIER;
					currentRelationshipType = AppConstants.PAIRING_NOTIFY_RELATIONSHIP;
					new Thread(new Runnable() {					
						@Override
						public void run() {
							removeRelationship(getAppEntity(), getPurifierEntity(), currentRelationshipType);
						}
					}).start();
				} 
			}else {
				if (entity_state== ENTITY.PURIFIER) {
					ALog.i(ALog.PAIRING, "Outgoing notify relationship (one removed) - Need to remove the other");
					entity_state= ENTITY.APP;
					new Thread(new Runnable() {					
						@Override
						public void run() {
							removeRelationship(getPurifierEntity(), getAppEntity(), currentRelationshipType);
						}
					}).start();

				} else if (entity_state== ENTITY.APP) {
					ALog.i(ALog.PAIRING, "NOTIFY Relationship removed successfully - Pairing removed successfully");
					notifyListenerSuccess();
					ALog.i(ALog.PAIRING, "Paring status set to UNPAIRED");
					purifier.setPairing(PurAirDevice.PAIRED_STATUS.UNPAIRED);
					purifierDatabase.updatePairingStatus(purifier, PurAirDevice.PAIRED_STATUS.UNPAIRED);
				} 
			}
		}

		else if (eventType == Commands.PAIRING_ADD_RELATIONSHIP) {
			ALog.i(ALog.PAIRING, "AddRelation call-SUCCESS");
			String relationStatus = pairingObj.getAddRelationStatus();
			if (relationStatus.equalsIgnoreCase("completed")) {

				if (currentRelationshipType.equals(AppConstants.PAIRING_DI_COMM_RELATIONSHIP)) {
					ALog.i(ALog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");
					currentRelationshipType = AppConstants.PAIRING_NOTIFY_RELATIONSHIP;
					addRelationship(AppConstants.PAIRING_NOTIFY_RELATIONSHIP, null);
				} 
				else {
					ALog.i(ALog.PAIRING, "Notification relationship added successfully - Pairing completed");
					notifyListenerSuccess();
					ALog.i(ALog.PAIRING, "Paring status set to true");
					purifier.setPairing(PurAirDevice.PAIRED_STATUS.PAIRED);
					purifier.setLastPairedTime(new Date().getTime()) ;
					purifierDatabase.updatePairingStatus(purifier, PurAirDevice.PAIRED_STATUS.PAIRED);
					/*if( updated != -1 ) {
						PurifierManager.getInstance().setCurrentPurifier(purifier) ;
					}*/

				}
			} else {
				ALog.i(ALog.PAIRING, "Pairing status is PENDING");
				notifyListenerFailed();
			}
		}
		else if(eventType== Commands.PAIRING_ADD_PERMISSIONS){					
			permissionListener.onPermissionAdded();
		}
		else if(eventType== Commands.PAIRING_GET_PERMISSIONS){
			boolean permissionExists=false;
			for(int i = 0; i < pairingObj.getNumberOfPermissionsReturned();i++)
			{
				permissionExists= pairingObj.getPermissionAtIndex(i).equals("Push");
				if(permissionExists)break;

			}
			permissionListener.onPermissionReturned(permissionExists);

		}else if(eventType==Commands.PAIRING_REMOVE_PERMISSIONS){
			permissionListener.onPermissionRemoved();
		}
	}

	private int getNumberOfRelationships(PairingService pairingObj) {
		int noOfRelationReturned=0;
		for(int i=0; i<pairingObj.getNumberOfRelationsReturned();i++){
			PairingReceivedRelationships relation= pairingObj.getReceivedRelationsAtIndex(i);
			PairingEntity entity= relation.pairingRcvdRelEntityTo;
			if(entity.PairingEntityId.equalsIgnoreCase(purifier.getEui64()))
				noOfRelationReturned++;
		}
		return noOfRelationReturned;
	}

	/**
	 * Method receiveServerResponse.
	 * @param responseCode int
	 * @param responseData String
	 * @see com.philips.cl.di.dev.pa.util.ServerResponseListener#receiveServerResponse(int, String)
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String fromIp) {
		ALog.d(ALog.PAIRING, "Purifier response: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			ALog.e(ALog.PAIRING, "PairingPort call-SUCCESS");
			addRelationship(currentRelationshipType, secretKey);
		} else {
			ALog.e(ALog.PAIRING, "PairingPort call-FAILED");
			notifyListenerFailed();
		}
	}

	private void notifyListenerSuccess() {
		if (pairingListener == null) return;
		pairingListener.onPairingSuccess();
	}

	private void notifyListenerFailed() {
		if (pairingListener == null) return;
		pairingListener.onPairingFailed();
	}


	/**
	 * Method addPermission- adds permission to a existing relationship
	 * @param relationType String
	 * @param permission String[]
	 */
	public void addPermission(String relationType, String[] permission){
		if(!cppController.isSignOn())return;
		PairingService addPermission = new PairingService(callbackHandler);
		int retStatus;
		retStatus = addPermission.addPermissionsRequest(getPurifierEntity(), relationType, permission);
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);
			return;
		}
		addPermission.setPairingServiceCommand(Commands.PAIRING_ADD_PERMISSIONS);
		retStatus = addPermission.executeCommand();
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);			
		}
	}

	/**
	 * Method getPermission-get permissions of a existing relationship
	 * @param relationType String
	 * @param permission String[]
	 */
	public void getPermission(String relationType, String[] permission){
		if(!cppController.isSignOn())return;
		int    iMaxPermissons = 5;
		int    iPermIndex = 0;
		PairingService getPermission = new PairingService(callbackHandler);
		int retStatus;

		retStatus = getPermission.getPermissionsRequest(null, getPurifierEntity(), relationType, iMaxPermissons, iPermIndex);
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);
			return;
		}
		getPermission.setPairingServiceCommand(Commands.PAIRING_GET_PERMISSIONS);
		retStatus = getPermission.executeCommand();
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);

		}
	}

	/**
	 * Method removePermission-remove permission from a existing relationship
	 * @param relationType String
	 * @param permission String[]
	 */
	public void removePermission(String relationType, String[] permission){
		if(!cppController.isSignOn()) return;
		PairingService removePermissions = new PairingService(callbackHandler);
		int retStatus;

		retStatus = removePermissions.removePermissionsRequest(getPurifierEntity(), relationType, permission);
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);
			return;
		}
		removePermissions.setPairingServiceCommand(Commands.PAIRING_REMOVE_PERMISSIONS);
		retStatus = removePermissions.executeCommand();
		if(Errors.SUCCESS != retStatus)
		{
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: "+retStatus);			
		}
	}

}
