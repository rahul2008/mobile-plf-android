/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

/**
 * Data-Creator Interface for creating the DataBase Objects
 */
public interface BaseAppDataCreator {

    /**
     * Creates a Moment Object
     *
     * @param creatorId The User-UUID from User Object (User-Registration Component)
     * @param subjectId The User-UUID from User Object (User-Registration Component)
     * @param type      Moment Type
     * @return returns a moment object created.
     */
    @NonNull
    Moment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull final String type, final DateTime expirationDate);

    /**
     * Creates a MomentDetail Object
     *
     * @param type   Type of the MomentDetail
     * @param moment The Moment to which the MomentDetail has to be attached
     * @return returns the momentDetail Object
     */
    @NonNull
    MomentDetail createMomentDetail(@NonNull final String type, @NonNull final Moment moment);

    /**
     * Creates a Measurement Object
     *
     * @param type             The type of the Measurement
     * @param measurementGroup The MeasurementGroup to which the Measurement has to be attached
     * @return returns a measurement object
     */
    Measurement createMeasurement(@NonNull final String type, @NonNull final MeasurementGroup measurementGroup);

    /**
     * Creates a MeasurementDetail Object
     *
     * @param type        The type of MeasurementDetail
     * @param measurement The Measurement to which the MeasurementDetail has to be attached.
     * @return returns a MeasurementDetail object
     */
    @NonNull
    MeasurementDetail createMeasurementDetail(@NonNull final String type, @NonNull final Measurement measurement);

    /**
     * Creates the MeasurementGroup Object
     *
     * @param measurementGroup The MeasurementGroup to which the MeasurementGroup has to be attached
     * @return returns a measurementGroup Object
     */
    @NonNull
    MeasurementGroup createMeasurementGroup(@NonNull final MeasurementGroup measurementGroup);

    /**
     * Creates a MeasurementGroup Object
     *
     * @param moment The Moment to which the MeasurementGroup has to be attached
     * @return
     */
    @NonNull
    MeasurementGroup createMeasurementGroup(@NonNull final Moment moment);

    /**
     * Creates a MeasurementGroupDetail Object
     *
     * @param type             The type of the MeasurementGroupDetail
     * @param measurementGroup The MeasurementGroup to which the MeasurementGroupDetail has to be attached
     * @return returns the MeasurementGroupDetail Object
     */
    @NonNull
    MeasurementGroupDetail createMeasurementGroupDetail(@NonNull final String type, @NonNull final MeasurementGroup measurementGroup);

    /**
     * Creates the SynchronizationData Object
     *
     * @param guid             The guid of the Moment (The MomentId returned from Server)
     * @param inactive         The inactive field of the moment (inactive = true: means deleted on server, inactive = false: means not deleted on server)
     * @param lastModifiedTime The lastModified time that comes from server response
     * @param version          version of the moment that comes from server
     * @return returns the synchronization object
     */
    @NonNull
    SynchronisationData createSynchronisationData(@NonNull final String guid, final boolean inactive, @NonNull final DateTime lastModifiedTime, final int version);

    /**
     * Creates a consentDetail Object
     *
     * @param type                       The type of the consentDetail
     * @param status                     the status of the consentDetail (Ex: refused/ Accepted)
     * @param version                    Document Version of the consentDetail (default: draft )
     * @param deviceIdentificationNumber deviceIdentificationNumber of the consent detail (default: manual)
     * @return returns the ConsentDetail Object
     */
    @NonNull
    ConsentDetail createConsentDetail(@NonNull final String type, @NonNull final String status, @NonNull final String version, final String deviceIdentificationNumber);

    /**
     * Creates the Setting Object
     *
     * @param type  The Type of the User Setting
     * @param value The Value of the User Setting
     * @return returns the User Settings Object created.
     */
    @NonNull
    Settings createSettings(String type, String value);

    /**
     * Creates the UserCharacteristics Object
     *
     * @param type            The type of UserCharacteristics
     * @param value           The value of UserCharacteristics
     * @param characteristics the UserCharacteristics Object to which the UserCharacteristics object has to be attached
     * @return returns the UserCharacteristics Object
     */
    @NonNull
    Characteristics createCharacteristics(@NonNull final String type, @NonNull final String value, @NonNull final Characteristics characteristics);

    /**
     * Creates the UserCharacteristics Object
     *
     * @param type  The Type of the UserCharacteristics
     * @param value The value of the UserCharacteristics
     * @return returns the UserCharacteristics Object
     */
    @NonNull
    Characteristics createCharacteristics(@NonNull final String type, @NonNull final String value);

    /**
     * Creates the Insight Object
     *
     * @return returns the Insight Object created
     */
    @NonNull
    Insight createInsight();

    /**
     * Creates the Insight metaData Object
     *
     * @param key     MetaData Key
     * @param value   MetaData Value
     * @param insight The Insight to which the metaData has to be attached
     * @return returns the insightMetaData Object
     */
    @NonNull
    InsightMetadata createInsightMetaData(String key, String value, Insight insight);
}
