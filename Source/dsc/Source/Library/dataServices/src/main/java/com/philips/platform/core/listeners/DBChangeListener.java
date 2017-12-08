/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

import com.philips.platform.core.datatypes.SyncType;

/**
 * Interface Callback for notifying DB change  success or failure
 */
public interface DBChangeListener {

    /**
     * Used for Notifying the Propositions in case of DB Change Success.
     * The DB change notification is sent to Propositions only in case the DB changes due to change on server data.
     * @param type The DataType that got changed (Ex: SyncType.Moment)
     */
    void dBChangeSuccess(SyncType type);

    /**
     * Used for Notifying the Propositions in case of DB Change Failed.
     * The DB change notification is sent to Propositions only in case the DB changes due to change on server data.
     * @param e Exception occurred while saving the data to Data-Base
     */
    void dBChangeFailed(Exception e);

}
