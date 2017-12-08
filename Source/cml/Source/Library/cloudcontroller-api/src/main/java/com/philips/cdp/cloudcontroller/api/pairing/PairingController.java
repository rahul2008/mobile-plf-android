/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.pairing;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The interface PairingController.
 *
 * Provides methods to configure pairing relationship and permissions.
 */
public interface PairingController {

    /**
     * The interface PairingCallback.
     * <p>
     * Used for notifications of pairing events. For this, set a callback on the
     * {@link PairingController} instance using the
     * {@link PairingController#setPairingCallback(PairingCallback)} method.
     */
    interface PairingCallback {
        /**
         * On relationship add.
         *
         * @param relationStatus the relation status
         */
        void onRelationshipAdd(String relationStatus);

        /**
         * On relationship remove.
         */
        void onRelationshipRemove();

        /**
         * On permissions add.
         */
        void onPermissionsAdd();

        /**
         * On permissions remove.
         */
        void onPermissionsRemove();

        /**
         * On permissions get.
         *
         * @param permissions the permissions
         */
        void onPermissionsGet(Collection<String> permissions);

        /**
         * On pairing error.
         *
         * @param eventType the event type
         * @param status    the status
         */
        void onPairingError(int eventType, int status);
    }

    String PERMISSION_RESPONSE = "Response";
    String PERMISSION_CHANGE = "Change";
    String PERMISSION_PUSH = "Push";

    List<String> PAIRING_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_RESPONSE, PERMISSION_CHANGE));
    List<String> PAIRING_PUSH_PERMISSIONS = Collections.unmodifiableList(Arrays.asList(PERMISSION_PUSH));

    /**
     * Sets the single pairing callback.
     *
     * @param pairingCallback the pairing callback
     */
    void setPairingCallback(@NonNull PairingCallback pairingCallback);

    /**
     * Add relationship.
     *
     * @param relationship   the relationship to be added
     * @param callback       the callback
     */
    void addRelationship(@NonNull PairingRelation relationship, @NonNull PairingCallback callback);

    /**
     * Add relationship.
     *
     * @param secretKey     the secret key
     * @param relationship  the relationship to be added
     * @param callback      the callback
     */
    void addRelationship(@NonNull PairingRelation relationship, @NonNull PairingCallback callback, String secretKey);

    /**
     * Remove relationship.
     *
     * @param relationship the relationship to be removed
     * @param callback     the callback
     */
    void removeRelationship(@NonNull PairingRelation relationship, @NonNull PairingCallback callback);

    /**
     * Add permission.
     *
     * @param relationType the relation type
     * @param permission   the permission
     * @param trustee      the trustee
     * @param callback     the callback
     */
    void addPermission(String relationType, String[] permission, PairingEntity trustee, @NonNull PairingCallback callback);

    /**
     * Remove permission.
     *
     * @param relationType the relation type
     * @param permission   the permission
     * @param trustee      the trustee
     * @param callback     the callback
     */
    void removePermission(String relationType, String[] permission, PairingEntity trustee, @NonNull PairingCallback callback);

    /**
     * Gets permission.
     *
     * @param relationType       the relation type
     * @param permission         the permission
     * @param trustee            the trustee
     * @param permissionListener the permission listener
     * @param callback           the callback
     */
    void getPermission(String relationType, String[] permission, PairingEntity trustee, PermissionListener permissionListener, @NonNull PairingCallback callback);
}
