/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

interface ISubjectProfileListener {

    void onSuccess(List<UCoreSubjectProfile> subjectProfileList);

    void onError(String errorMessage);
}
