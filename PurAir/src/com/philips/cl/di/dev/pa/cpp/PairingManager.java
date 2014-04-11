package com.philips.cl.di.dev.pa.cpp;

import java.net.HttpURLConnection;

import android.content.Context;

import com.philips.cl.di.dev.pa.constant.AppConstants;
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
import com.philips.icpinterface.data.PairingRelationship;

public class PairingManager implements ICPEventListener, ServerResponseListener {

	private PairingEntitiyReference pairingTrustor = null;
	private PairingEntitiyReference pairingTrustee = null;
	private PairingEntitiyReference pairingDelegator = null;
	private PairingEntitiyReference pairingTarget = null;
	private PairingInfo pairingTypeInfo = null;
	private PairingRelationship pairingRelationshipData = null;
	private ICPCallbackHandler callbackHandler;
	private Context context;
	private String purifierEui64 = null;
	private String strRelType = null;
	private PairingListener pairingListener;
	private PurifierDatabase purifierDatabase;
	private String secretKey;

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
	public PairingManager(Context context, PairingListener iPairingListener,
			String purifierEui64) {
		this.context = context;
		this.purifierEui64 = purifierEui64;
		purifierDatabase = new PurifierDatabase(context);
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
	public void startPairing(String relationshipType, String[] permission) {
			strRelType = relationshipType;
			getRelationship(relationshipType, purifierEui64);
	}

	/**
	 * Method getRelationship- fetches existing relationships
	 * 
	 * @param relationshipType
	 *            String
	 * @param permission
	 *            String[]
	 * @param purifierEui64
	 *            String
	 */
	private void getRelationship(String relationshipType, String purifierEui64) {

		pairingTarget = addTrustee(purifierEui64, pairingTarget);
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
		retValue = getRelations.getRelationShipRequest(pairingTarget,
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
			final String[] permission, Context context) {

		if (relationshipType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
			secretKey = generateRandomSecretKey();
			String pairing_url = String.format(AppConstants.URL_PAIRING_PORT,
					Utils.getIPAddress(context));
			String dataToUpload = JSONBuilder.getDICOMMPairingJSON(secretKey);
			dataToUpload = new DISecurity(null).encryptData(dataToUpload,
					AppConstants.deviceId);
			TaskPutDeviceDetails pairingRunnable = new TaskPutDeviceDetails(
					dataToUpload, pairing_url, this);
			Thread pairingThread = new Thread(pairingRunnable);
			pairingThread.start();

		} else {
			strRelType = relationshipType;
			addRelationship(relationshipType, permission, null);
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
	private void addRelationship(String relationshipType, String[] permission,
			String secretKey) {

		addPairingRelationshipData(relationshipType, permission);
		pairingTrustee = addTrustee(purifierEui64, pairingTrustee);
		if (secretKey != null) {
			addPairingInfo(secretKey);
		}

		int status;
		PairingService addPSRelation = new PairingService(callbackHandler);
		addPSRelation.addRelationShipRequest(pairingTrustor, pairingTrustee,
				pairingDelegator, pairingRelationshipData, pairingTypeInfo);

		addPSRelation
		.setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
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
	 */
	private void addPairingRelationshipData(String relationshipType,
			String[] permission) {
		if (null == pairingRelationshipData) {
			pairingRelationshipData = new PairingRelationship();
		}
		pairingRelationshipData.pairingRelationshipIsAllowDelegation = true;
		pairingRelationshipData.pairingRelationshipMetadata = null;
		pairingRelationshipData.pairingRelationshipRelationType = relationshipType;
		pairingRelationshipData.pairingRelationshipTTL = 12;
		pairingRelationshipData.pairingRelationshipPermissionArray = permission;
	}

	/**
	 * add Trustee data
	 * 
	 * @param purifierEui64
	 * @param pairingTrustee
	 * 
	 * @return PairingEntitiyReference
	 */
	private PairingEntitiyReference addTrustee(String purifierEui64,
			PairingEntitiyReference pairingTrustee) {
		if (pairingTrustee == null) {
			pairingTrustee = new PairingEntitiyReference();
		}
		pairingTrustee.entityRefId = purifierEui64;
		pairingTrustee.entityRefProvider = "cpp";
		pairingTrustee.entityRefType = "AC4373GENDEV";
		pairingTrustee.entityRefCredentials = null;
		return pairingTrustee;
	}

	/**
	 * add pairing info
	 * 
	 * @param secretKey
	 */
	private void addPairingInfo(String secretKey) {
		if (pairingTypeInfo == null) {
			pairingTypeInfo = new PairingInfo();
		}
		pairingTypeInfo.pairingInfoIsMatchIPAddr = false;
		pairingTypeInfo.pairingInfoRequestTTL = 1234;
		pairingTypeInfo.pairingInfoSecretKey = secretKey;
	}

	/**
	 * generates random key
	 * 
	 * 
	 * @return random secret key
	 */
	private String generateRandomSecretKey() {
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
	 *      int, ICPClient)
	 */
	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		ALog.i(ALog.PAIRING, "onICPCallbackEventOccurred eventType "
				+ eventType + " status " + status);

		if (eventType == Commands.PAIRING_GET_RELATIONSHIPS) {
			if (status == Errors.SUCCESS) {
				ALog.i(ALog.PAIRING, "GetRelation-SUCCESS");
				PairingService pairingObj = (PairingService) obj;
				int relations = pairingObj.getNumberOfRelationsReturned();
				if (relations < 1) {
					startPairingPortTask(strRelType, AppConstants.PERMISSIONS,
							context);
				} else if (strRelType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
					strRelType = AppConstants.NOTIFY_RELATIONSHIP;
					getRelationship(strRelType, purifierEui64);
					ALog.i(ALog.PAIRING, "Started AddRelation-NOTIFY");
				} else if (strRelType.equals(AppConstants.NOTIFY_RELATIONSHIP)) {
					purifierDatabase.updatePairingStatus(purifierEui64);
					pairingListener.onPairingStateReceived(status, eventType,
							obj);
					ALog.i(ALog.PAIRING, "GetRelation-AlreadyPaired");
				}
			} else {
				pairingListener.onPairingStateReceived(status, eventType, obj);
				ALog.e(ALog.PAIRING, "GetRelation-FAILED");
			}
		} else if (eventType == Commands.PAIRING_ADD_RELATIONSHIP) {
			if (status == Errors.SUCCESS) {
				PairingService pairingObj = (PairingService) obj;
				String relationStatus = pairingObj.getAddRelationStatus();
				if (relationStatus.equalsIgnoreCase("completed")) {
					ALog.i(ALog.PAIRING, "AddRelation-SUCCESS");
					if (strRelType.equals(AppConstants.DI_COMM_RELATIONSHIP)) {
						strRelType = AppConstants.NOTIFY_RELATIONSHIP;
						addRelationship(AppConstants.NOTIFY_RELATIONSHIP,
								AppConstants.NOTIFY_PERMISSIONS, null);
						ALog.i(ALog.PAIRING, "Started AddRelation-NOTIFY");
					} else {
						pairingListener.onPairingStateReceived(status,
								eventType, obj);
						purifierDatabase.updatePairingStatus(purifierEui64);
					}
				} else {
					pairingListener.onPairingStateReceived(status, eventType,
							obj);
				}
			} else {
				pairingListener.onPairingStateReceived(status, eventType, obj);
				ALog.e(ALog.PAIRING, "AddRelation-FAILED");
			}
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		ALog.d(ALog.PAIRING, "responseCode: " + responseCode + "responseData: "
				+ responseData);
		if (responseCode == HttpURLConnection.HTTP_OK) {

			addRelationship(strRelType, AppConstants.PERMISSIONS, secretKey);
		} else {
			pairingListener.onPairingPortTaskFailed();
			ALog.e(ALog.PAIRING, "pairingPort-FAILED");
		}
	}

}
