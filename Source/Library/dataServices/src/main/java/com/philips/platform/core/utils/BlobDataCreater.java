package com.philips.platform.core.utils;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.datasync.blob.BlobMetaData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface BlobDataCreater extends BaseAppDataCreator{
    BlobMetaData createBlobMetaData();
}
