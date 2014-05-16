package com.philips.cl.di.dev.pa.cpp;

import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
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
public class PairingManager implements ICPEventListener, ServerResponseListener {

	private static final int PAIRING_TTL = 480;  // 8 hours
	private static final int PAIRING_REQUESTTTL = 5; // ingored by cpp, because purifier already defined it
	private static final String PAIRING_REFERENCETYPE = "AC4373GENDEV";
	private static final String PAIRING_REFERENCEPROVIDER = "cpp";
	
	private ICPCallbackHandler callbackHandler;
	private String currentRelationshipType = null;
	private PairingListener pairingListener;
	private PurifierDatabase purifierDatabase;
	private String secretKey;
	
	private PurAirDevice purifier;

	/**
	 * Constructor for PairingManager.
	 * 
	 * @param context
	 *            Context
	 * @param iPairingListener
	 *            PairingListener
	 * @param purifierEui64
	 *            String
	 */
	public PairingManager(PairingListener iPairingListener,	PurAirDevice purifier) {
		this.purifier = purifier;
		purifierDatabase = new PurifierDatabase();
		pairingListener = iPairingListener;
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
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
		currentRelationshipType = AppConstants.DI_COMM_RELATIONSHIP;
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
		retValue = getRelations.getRelationShipRequest(getPurifierEntity(),
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

		if (relationshipType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
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

		int status;
		PairingService addPSRelation = new PairingService(callbackHandler);
		
		if (secretKey != null && relationshipType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
			addPSRelation.addRelationShipRequest(null, getPurifierEntity(),
				null, getPairingRelationshipData(relationshipType, AppConstants.PERMISSIONS.toArray(new String[AppConstants.PERMISSIONS.size()])), getPairingInfo(secretKey));
		} else {
			addPSRelation.addRelationShipRequest(null, getPurifierEntity(),
					null, getPairingRelationshipData(relationshipType, AppConstants.NOTIFY_PERMISSIONS.toArray(new String[AppConstants.NOTIFY_PERMISSIONS.size()])), null);
		}

		addPSRelation.setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
		status = addPSRelation.executeCommand();
		if (Errors.SUCCESS != status) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: ");
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
		pairingRelationshipData.pairingRelationshipTTL = PAIRING_TTL;
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
		pairingTrustee.entityRefProvider = PAIRING_REFERENCEPROVIDER;
		pairingTrustee.entityRefType = PAIRING_REFERENCETYPE;
		pairingTrustee.entityRefCredentials = null;
		return pairingTrustee;
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
		pairingTypeInfo.pairingInfoRequestTTL = PAIRING_REQUESTTTL;
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
			ALog.e(ALog.PAIRING, "Pairing call-FAILED (get or add)");
			notifyListenerFailed();
			return;
		}
		
		PairingService pairingObj = (PairingService) obj;
		if (eventType == Commands.PAIRING_GET_RELATIONSHIPS) {			
			ALog.i(ALog.PAIRING, "GetRelation call-SUCCESS");
			int noOfRelations= getNumberOfRelation(pairingObj);
			int diCommRelationships = pairingObj.getNumberOfRelationsReturned();
			if (diCommRelationships < 1 || noOfRelations<1) {
				ALog.i(ALog.PAIRING, "No existing relationships - Requesting Purifier to start pairing");
				startPairingPortTask(currentRelationshipType, AppConstants.PERMISSIONS.toArray(new String[AppConstants.PERMISSIONS.size()]));
			} 
			else if (diCommRelationships < 2 || noOfRelations<1) {
				ALog.i(ALog.PAIRING, "Only one existing relationship (one expired) - Need to start pairing again");
				startPairingPortTask(currentRelationshipType, AppConstants.PERMISSIONS.toArray(new String[AppConstants.PERMISSIONS.size()]));
			} 
			else if (currentRelationshipType.equals(AppConstants.DI_COMM_RELATIONSHIP) && noOfRelations>0) {
				currentRelationshipType = AppConstants.NOTIFY_RELATIONSHIP;
				getRelationship(currentRelationshipType, purifier.getEui64());
				ALog.i(ALog.PAIRING, "DI COMM relationship exists, checking for notify relationship");
			} 
			else if (currentRelationshipType.equals(AppConstants.NOTIFY_RELATIONSHIP) && noOfRelations>0) {
				purifier.setPairing(true);
				purifierDatabase.updatePairingStatus(purifier);
				ALog.i(ALog.PAIRING, "Notify relationship exists, pairing already successfull");
				notifyListenerSuccess();
			}
		} 
		
		else if (eventType == Commands.PAIRING_ADD_RELATIONSHIP) {
			ALog.i(ALog.PAIRING, "AddRelation call-SUCCESS");
			String relationStatus = pairingObj.getAddRelationStatus();
			if (relationStatus.equalsIgnoreCase("completed")) {
				
				if (currentRelationshipType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
					ALog.i(ALog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");
					currentRelationshipType = AppConstants.NOTIFY_RELATIONSHIP;
					addRelationship(AppConstants.NOTIFY_RELATIONSHIP, null);
				} 
				else {
					ALog.i(ALog.PAIRING, "Notification relationship added successfully - Pairing completed");
					notifyListenerSuccess();
					purifier.setPairing(true);
					purifierDatabase.updatePairingStatus(purifier);
				}
			} else {
				ALog.i(ALog.PAIRING, "Pairing status is PENDING");
				notifyListenerFailed();
			}
		}
	}

	private int getNumberOfRelation(PairingService pairingObj) {
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
}
