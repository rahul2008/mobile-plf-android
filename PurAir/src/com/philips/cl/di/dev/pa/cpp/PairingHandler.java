package com.philips.cl.di.dev.pa.cpp;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.philips.cdp.dicomm.cpp.CPPController;
import com.philips.cdp.dicomm.cpp.ICPCallbackHandler;
import com.philips.cdp.dicomm.cpp.ICPEventListener;
import com.philips.cdp.dicomm.util.ListenerRegistration;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.PermissionListener;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;
import com.philips.icpinterface.data.PairingInfo;
import com.philips.icpinterface.data.PairingReceivedRelationships;
import com.philips.icpinterface.data.PairingReceivedRelationships.PairingEntity;
import com.philips.icpinterface.data.PairingRelationship;

public class PairingHandler implements ICPEventListener {

	/** PAIRING CONSTANTS */
	public static final int PAIRING_RELATIONSHIPDURATION_SEC = 1000000000;  // 8 hours
	public static final int PAIRING_REQUESTTTL_MIN = 5; // ingored by cpp, because purifier already defined it

	public static final String PAIRING_REFERENCEPROVIDER = "cpp";
	public static final String PAIRING_DI_COMM_RELATIONSHIP="DI-COMM";
	public static final String PAIRING_NOTIFY_RELATIONSHIP="NOTIFY";
	public static final String PAIRING_DATA_ACCESS_RELATIONSHIP = "DATAACCESS";

	public static final int MAX_RETRY = 3;

	public static final List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Response", "Change"));
	public static final List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList("Push"));

	private ICPCallbackHandler callbackHandler;
	private String currentRelationshipType ;
	private PairingListener pairingListener;
	private String secretKey;

	private PermissionListener permissionListener = null;
	private CPPController cppController;
	private static HashMap<String, Integer> attemptsCount = new HashMap<String, Integer>();

	private enum ENTITY {
		PURIFIER, APP, DATAACCESS
	};

	private ENTITY entity_state;
    private DICommAppliance mAppliance;

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
	public PairingHandler(PairingListener iPairingListener,	DICommAppliance appliance) {
	    if(appliance==null)return;
		this.mAppliance = appliance;
		pairingListener = iPairingListener;
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
		cppController = CPPController.getInstance();
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
		if(mAppliance==null)return;
		ALog.i(ALog.PAIRING, "Started pairing with appliance = " + mAppliance.getNetworkNode().getName() + "attempt: "+getPairingAttempts(mAppliance.getNetworkNode().getCppId()));
		currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
//		getRelationship(currentRelationshipType);
		startPairingPortTask(currentRelationshipType, PAIRING_PERMISSIONS
                .toArray(new String[PAIRING_PERMISSIONS.size()]));
	}

	/**
	 * Method getRelationship- fetches existing relationships
	 * 
	 * @param relationshipType
	 *            String
	 */
	private void getRelationship(String relationshipType) {
		ALog.i(ALog.PAIRING, "Requesting existing relationships");

		if (!cppController.isSignOn())	return;

		ALog.i(ALog.PAIRING, "signOn is success");
		boolean bincludeIncoming = true;
		boolean bincludeOutgoing = true;
		int iMetadataSize = 0;
		int iMaxPermissions = 10;
		int iMaxRelations = 5;
		int iRelOffset = 0;
		int retValue = 0;
		PairingService getRelations = new PairingService(callbackHandler);

		getRelations.setPairingServiceCommand(Commands.PAIRING_GET_RELATIONSHIPS);
		retValue = getRelations.getRelationshipRequest(null,
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

		if (relationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)) {
			if(mAppliance==null)return;
			secretKey = generateRandomSecretKey();
			String appEui64 = cppController.getAppCppId();
			
			PairingPort pairingPort = mAppliance.getPairingPort();
			pairingPort.registerPortListener(new DICommPortListener() {
                
                @Override
                public ListenerRegistration onPortUpdate(DICommPort<?> port) {
                    ALog.i(ALog.PAIRING, "PairingPort call-SUCCESS");
                    addRelationship(currentRelationshipType, secretKey);
                    return ListenerRegistration.UNREGISTER;
                }
                
                @Override
                public ListenerRegistration onPortError(DICommPort<?> port, Error error, String errorData) {
                    ALog.e(ALog.PAIRING, "PairingPort call-FAILED");
                    notifyListenerFailed(true);
                    return ListenerRegistration.UNREGISTER;
                }
            });
            pairingPort.triggerPairing(CPPController.getInstance().getAppType(), appEui64, secretKey);

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

		if (!cppController.isSignOn()) return;
		int status;
		PairingService addPSRelation = new PairingService(callbackHandler);

		if (secretKey != null
				&& relationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)) {
			addPSRelation.addRelationshipRequest(null, getDICommApplianceEntity(), null, getPairingRelationshipData(
					relationshipType, PAIRING_PERMISSIONS.toArray(
							new String[PAIRING_PERMISSIONS.size()])), getPairingInfo(secretKey));
		} else {
			addPSRelation.addRelationshipRequest(null, getDICommApplianceEntity(), null, getPairingRelationshipData(
					relationshipType,PAIRING_PUSH_PERMISSIONS.toArray(
							new String[PAIRING_PUSH_PERMISSIONS.size()])), null);
		}

		addPSRelation.setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
		status = addPSRelation.executeCommand();
		if (Errors.SUCCESS != status) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: ");
		}
	}

	public void initializeRelationshipRemoval() {
		currentRelationshipType = PAIRING_DI_COMM_RELATIONSHIP;
		entity_state = ENTITY.PURIFIER;
		removeRelationship(null, getDICommApplianceEntity(), currentRelationshipType);
	}

	/**
	 * Method removeRelationship-remove an existing relationship
	 * 
	 * @param relationType
	 *            String
	 */
	private void removeRelationship(PairingEntitiyReference trustor,
			PairingEntitiyReference trustee, String relationType) {
		if (!cppController.isSignOn())
			return;
		PairingService removeRelationship = new PairingService(callbackHandler);
		int retStatus;

		retStatus = removeRelationship.removeRelationshipRequest(trustor, trustee, relationType);
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
			return;
		}
		removeRelationship.setPairingServiceCommand(Commands.PAIRING_REMOVE_RELATIONSHIP);
		retStatus = removeRelationship.executeCommand();
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
		}
	}

	/**
	 * add pairingRelationshipData
	 * 
	 * @param permission
	 * @param relationshipType
	 * @return
	 */
	private PairingRelationship getPairingRelationshipData(
			String relationshipType, String[] permission) {
		PairingRelationship pairingRelationshipData = new PairingRelationship();
		pairingRelationshipData.pairingRelationshipIsAllowDelegation = true;
		pairingRelationshipData.pairingRelationshipMetadata = null;
		pairingRelationshipData.pairingRelationshipRelationType = relationshipType;
		pairingRelationshipData.pairingRelationshipTTL = PAIRING_RELATIONSHIPDURATION_SEC;
		pairingRelationshipData.pairingRelationshipPermissionArray = permission;
		return pairingRelationshipData;
	}

	/**
	 * add Trustee data
	 * 
	 * @param purifierEui64
	 * @param pairingTrustee
	 * 
	 * @return PairingEntitiyReference
	 */
	private PairingEntitiyReference getDICommApplianceEntity() {
		PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();
		pairingTrustee.entityRefId = mAppliance.getNetworkNode().getCppId();
		pairingTrustee.entityRefProvider = PAIRING_REFERENCEPROVIDER;
		pairingTrustee.entityRefType = mAppliance.getDeviceType();
		pairingTrustee.entityRefCredentials = null;

		ALog.i(ALog.PAIRING, "Appliance entityRefId"
				+ pairingTrustee.entityRefId);
		ALog.i(ALog.PAIRING, "Appliance entityRefType"
				+ pairingTrustee.entityRefType);

		return pairingTrustee;
	}

	/**
	 * add Trustee data
	 * 
	 * @param purifierEui64
	 * @param pairingTrustee
	 * 
	 * @return PairingEntitiyReference
	 */
	private PairingEntitiyReference getAppEntity() {
		PairingEntitiyReference pairingTrustor = new PairingEntitiyReference();
		pairingTrustor.entityRefId = cppController.getAppCppId();
		pairingTrustor.entityRefProvider = PAIRING_REFERENCEPROVIDER;
		pairingTrustor.entityRefType = CPPController.getInstance().getAppType();
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
		pairingTypeInfo.pairingInfoRequestTTL = PAIRING_REQUESTTTL_MIN;
		pairingTypeInfo.pairingInfoSecretKey = secretKey;
		return pairingTypeInfo;
	}

	/**
	 * generates random key	 *
	 * @return random secret key
	 */
	public String generateRandomSecretKey() {
		return Long.toHexString(Double.doubleToLongBits(Math.random()));

	}

	public static int getPairingAttempts(String eui64){
		int attempts=0;
		if(attemptsCount.containsKey(eui64)){
			attempts= attemptsCount.get(eui64);
		}
		return attempts;
	}
	
	public static void clear() {
		if( attemptsCount != null)
			attemptsCount.clear() ;
	}

	public void setPairingAttempts(String eui64){
		int attempts=0;
		if(attemptsCount.containsKey(eui64)){
			attempts= attemptsCount.get(eui64);
		}
		attemptsCount.put(eui64, attempts+1);
	}
	
	public void resetPairingAttempts(String eui64){
		attemptsCount.put(eui64, 0);
	}

	/**
	 * Method onICPCallbackEventOccurred.
	 * 
	 * @param eventType	 *            int
	 * @param status	 *            int
	 * @param obj	 *            ICPClient	 * 
	 * @see com.philips.cdp.dicomm.cpp.ICPEventListener#onICPCallbackEventOccurred(int,
	 *      int, ICPClient)
	 */
	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		ALog.d(ALog.PAIRING, "onICPCallbackEventOccurred eventType "
				+ eventType + " status " + status);

		if (status != Errors.SUCCESS) {
			if (permissionListener == null) {
				ALog.e(ALog.PAIRING, "Pairing call-FAILED (get or add), pairing attempt:"+ getPairingAttempts(mAppliance.getNetworkNode().getCppId())+" Appliance name:" +mAppliance.getNetworkNode().getName());
				notifyListenerFailed(false);

			} else {
				ALog.e(ALog.PAIRING, "get permission call failed");
				permissionListener.onCallFailed();
			}
			return;
		}

		PairingService pairingObj = (PairingService) obj;
		int diCommRelationships = pairingObj.getNumberOfRelationsReturned();
		if (eventType == Commands.PAIRING_GET_RELATIONSHIPS) {
			ALog.i(ALog.PAIRING, "GetRelation call-SUCCESS");
			int noOfRelations = getNumberOfRelationships(pairingObj);
			if (diCommRelationships < 1 || noOfRelations < 1) {

				ALog.i(ALog.PAIRING, "No existing relationships - Requesting Purifier to start pairing");
				startPairingPortTask(currentRelationshipType, PAIRING_PERMISSIONS
						.toArray(new String[PAIRING_PERMISSIONS.size()]));
			} else if (diCommRelationships < 2 || noOfRelations < 1) {

				ALog.i(ALog.PAIRING, "Only one existing relationship (one expired) - Need to start pairing again");
				startPairingPortTask(currentRelationshipType, PAIRING_PERMISSIONS
						.toArray(new String[PAIRING_PERMISSIONS.size()]));
			} else if (currentRelationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)
					&& noOfRelations > 0) {
				currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
				getRelationship(currentRelationshipType);
				ALog.i(ALog.PAIRING, "DI COMM relationship exists, checking for notify relationship");
			} else if (currentRelationshipType.equals(PAIRING_NOTIFY_RELATIONSHIP)
					&& noOfRelations > 0) {
				ALog.i(ALog.PAIRING, "Notify relationship exists, pairing already successfull");
				notifyListenerSuccess();
				ALog.i(ALog.PAIRING, "Paring status set to true");
				mAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
				mAppliance.getNetworkNode().setLastPairedTime(new Date().getTime());
				
				//TODO better solution
				DICommAppliance appliance = DiscoveryManager.getInstance().getApplianceByCppId(mAppliance.getNetworkNode().getCppId());

				//TODO verify with Jeroen: implementation correct this way?
				appliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
				DiscoveryManager.getInstance().updateApplianceInDatabase(appliance);

				//Clear indoor AQI historic data
				SessionDto.getInstance().setIndoorTrendDto(mAppliance.getNetworkNode().getCppId(), null);

			}
		}

		else if (eventType == Commands.PAIRING_REMOVE_RELATIONSHIP) {
			ALog.i(ALog.PAIRING, "RemoveRelation call-SUCCESS");
			if (currentRelationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)) {
				if (entity_state == ENTITY.PURIFIER) {
					ALog.i(ALog.PAIRING, "Outgoing di-comm relationship (one removed) - Need to remove the other");
					entity_state = ENTITY.APP;
					new Thread(new Runnable() {
						@Override
						public void run() {
							removeRelationship(getDICommApplianceEntity(),	getAppEntity(), currentRelationshipType);
						}
					}).start();

				} else if (entity_state == ENTITY.APP) {
					ALog.i(ALog.PAIRING, "DI-COMM Relationship removed successfully");
					entity_state = ENTITY.DATAACCESS;
					currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
					new Thread(new Runnable() {
						@Override
						public void run() {
							removeRelationship(getAppEntity(), getDICommApplianceEntity(),	currentRelationshipType);
						}
					}).start();
				}
			}
			else if(currentRelationshipType.equals(PAIRING_NOTIFY_RELATIONSHIP)) {
				if(entity_state == ENTITY.DATAACCESS) {
					ALog.i(ALog.PAIRING, "Notify Relationship removed successfully");
					entity_state = ENTITY.PURIFIER;
					currentRelationshipType = PAIRING_DATA_ACCESS_RELATIONSHIP;
					new Thread(new Runnable() {
						@Override
						public void run() {
							removeRelationship(null, getDICommApplianceEntity(), currentRelationshipType);
						}
					}).start();
				}
			}
			// This will indicate all relations have been removed
			else if(currentRelationshipType.equals(PAIRING_DATA_ACCESS_RELATIONSHIP)) {
				if (entity_state == ENTITY.PURIFIER) {
					ALog.i(ALog.PAIRING, "DATAACCESS Relationship removed successfully - Pairing removed successfully");
					notifyListenerSuccess();
				}
			}
		}

		else if (eventType == Commands.PAIRING_ADD_RELATIONSHIP) {
			ALog.i(ALog.PAIRING, "AddRelation call-SUCCESS");
			String relationStatus = pairingObj.getAddRelationStatus();
			if (relationStatus.equalsIgnoreCase("completed")) {

				if (currentRelationshipType.equals(PAIRING_DI_COMM_RELATIONSHIP)) {
					ALog.i(ALog.PAIRING, "Pairing relationship added successfully - Requesting Notification relationship");
					currentRelationshipType = PAIRING_NOTIFY_RELATIONSHIP;
					addRelationship(PAIRING_NOTIFY_RELATIONSHIP, null);
				} else {
					ALog.i(ALog.PAIRING, "Notification relationship added successfully - Pairing completed");
					ALog.i(ALog.PAIRING, "Paring status set to true");
					mAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
					mAppliance.getNetworkNode().setLastPairedTime(new Date().getTime());

					//TODO better solution
					DICommAppliance appliance = DiscoveryManager.getInstance().getApplianceByCppId(mAppliance.getNetworkNode().getCppId());

					//TODO verify with Jeroen: implementation correct this way?
					appliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
					DiscoveryManager.getInstance().updateApplianceInDatabase(appliance);

					notifyListenerSuccess();
				}
			} else {
				ALog.e(ALog.PAIRING, "Pairing status is PENDING, pairing attempt:"+ getPairingAttempts(mAppliance.getNetworkNode().getCppId())+" Appliance name:" +mAppliance.getNetworkNode().getName());
				notifyListenerFailed(false);

			}
		} else if (eventType == Commands.PAIRING_ADD_PERMISSIONS) {
			permissionListener.onPermissionAdded();
		} else if (eventType == Commands.PAIRING_GET_PERMISSIONS) {
			boolean permissionExists = false;
			for (int i = 0; i < pairingObj.getNumberOfPermissionsReturned(); i++) {
				permissionExists = pairingObj.getPermissionAtIndex(i).equals("Push");
				if (permissionExists) break;
			}
			permissionListener.onPermissionReturned(permissionExists);

		} else if (eventType == Commands.PAIRING_REMOVE_PERMISSIONS) {
			permissionListener.onPermissionRemoved();
		}
	}

	private int getNumberOfRelationships(PairingService pairingObj) {
		int noOfRelationReturned = 0;
		if (pairingObj == null)
			return noOfRelationReturned;

		for (int i = 0; i < pairingObj.getNumberOfRelationsReturned(); i++) {
			PairingReceivedRelationships relation = pairingObj
					.getReceivedRelationsAtIndex(i);
			PairingEntity entity = relation.pairingRcvdRelEntityTo;
			if (entity.PairingEntityId.equalsIgnoreCase(mAppliance.getNetworkNode().getCppId()))
				noOfRelationReturned++;
		}
		return noOfRelationReturned;
	}

	private void notifyListenerSuccess() {
		if (pairingListener == null) return;
		pairingListener.onPairingSuccess(mAppliance.getNetworkNode());
	}

	private void notifyListenerFailed(boolean isPairingPortTaskFailed) {

		if(mAppliance==null)return;
		if(getPairingAttempts(mAppliance.getNetworkNode().getCppId())<MAX_RETRY){
			setPairingAttempts(mAppliance.getNetworkNode().getCppId());
			// If DI-COMM local (Pairing Port) request fails, then retry only the DI-COMM request
			if( isPairingPortTaskFailed ) {
				startPairingPortTask(PAIRING_DI_COMM_RELATIONSHIP, PAIRING_PERMISSIONS
						.toArray(new String[PAIRING_PERMISSIONS.size()]));
			}
			else {
				startPairing();
			}
		}
		else {
			mAppliance.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
			if (pairingListener == null) return;
			pairingListener.onPairingFailed(mAppliance.getNetworkNode());
		}		
	}

	/**
	 * Method addPermission- adds permission to a existing relationship
	 * 
	 * @param relationType
	 *            String
	 * @param permission
	 *            String[]
	 */
	public void addPermission(String relationType, String[] permission) {
		if (!cppController.isSignOn())
			return;
		PairingService addPermission = new PairingService(callbackHandler);
		int retStatus;
		retStatus = addPermission.addPermissionsRequest(getDICommApplianceEntity(),
				relationType, permission);
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
			return;
		}
		addPermission
		.setPairingServiceCommand(Commands.PAIRING_ADD_PERMISSIONS);
		retStatus = addPermission.executeCommand();
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
		}
	}

	/**
	 * Method getPermission-get permissions of a existing relationship
	 * 
	 * @param relationType
	 *            String
	 * @param permission
	 *            String[]
	 */
	public void getPermission(String relationType, String[] permission) {
		if (!cppController.isSignOn()) {
			permissionListener.onCallFailed();
		}
		else {
			int iMaxPermissons = 5;
			int iPermIndex = 0;
			PairingService getPermission = new PairingService(callbackHandler);
			int retStatus;
	
			retStatus = getPermission.getPermissionsRequest(null,
					getDICommApplianceEntity(), relationType, iMaxPermissons, iPermIndex);
			if (Errors.SUCCESS != retStatus) {
				ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
				permissionListener.onCallFailed();
				return;
			}
			getPermission.setPairingServiceCommand(Commands.PAIRING_GET_PERMISSIONS);
			retStatus = getPermission.executeCommand();
			if (Errors.SUCCESS != retStatus) {
				permissionListener.onCallFailed();
				ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
	
			}
		}
	}

	/**
	 * Method removePermission-remove permission from a existing relationship
	 * 
	 * @param relationType
	 *            String
	 * @param permission
	 *            String[]
	 */
	public void removePermission(String relationType, String[] permission) {
		if (!cppController.isSignOn())
			return;
		PairingService removePermissions = new PairingService(callbackHandler);
		int retStatus;

		retStatus = removePermissions.removePermissionsRequest(getDICommApplianceEntity(),
				relationType, permission);
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
			return;
		}
		removePermissions
		.setPairingServiceCommand(Commands.PAIRING_REMOVE_PERMISSIONS);
		retStatus = removePermissions.executeCommand();
		if (Errors.SUCCESS != retStatus) {
			ALog.d(ALog.PAIRING, "Request Invalid/Failed Status: " + retStatus);
		}
	}

	public static long getDiffInDays(long pairedOn) {
		Date currentDate = new Date();
		long currenttimeInMillis = currentDate.getTime();

		// Difference between current and previous timestamp
		long diff = currenttimeInMillis - pairedOn;
		long diffInDays = diff / (1000 * 60 * 60 * 24);

		return diffInDays ;
	}

	public static boolean pairApplianceIfNecessary(NetworkNode networkNode) {
		if (networkNode == null || networkNode.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
			return false;
		}	
		

		ALog.i(ALog.PAIRING, "In PairToPurifier: "+ networkNode.getPairedState());
		
		// First time pairing or on EWS 
		if( networkNode.getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED ) {
			return true;			
		}
		//Everyday check for pairing
		long lastPairingCheckTime = networkNode.getLastPairedTime();
		long diffInDays = PairingHandler.getDiffInDays(lastPairingCheckTime);

		if(networkNode.getPairedState()==NetworkNode.PAIRED_STATUS.PAIRED && diffInDays != 0){
			return true;
		}
		return false;
	}

}
