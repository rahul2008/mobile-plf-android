package com.philips.platform.dscdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrmDeletingTest {

    private OrmDeleting ormDeleting;

    @Mock
    private Dao<OrmMoment, Integer> momentDaoMock;
    @Mock
    private Dao<OrmMomentDetail, Integer> momentDetailDaoMock;
    @Mock
    private Dao<OrmMeasurement, Integer> measurementDaoMock;
    @Mock
    private Dao<OrmMeasurementDetail, Integer> measurementDetailDaoMock;
    @Mock
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDaoMock;
    @Mock
    private Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailDaoMock;
    @Mock
    private Dao<OrmMeasurementGroup, Integer> measurementGroupsDaoMock;
    @Mock
    private Dao<OrmConsentDetail, Integer> constentDetailsDaoMock;
    @Mock
    private Dao<OrmCharacteristics, Integer> characteristicsesDaoMock;
    @Mock
    private Dao<OrmSettings, Integer> settingsDaoMock;
    @Mock
    private Dao<OrmDCSync, Integer> syncDaoMock;
    @Mock
    private Dao<OrmInsight, Integer> ormInsightDaoMock;
    @Mock
    private Dao<OrmInsightMetaData, Integer> ormInsightMetadataDaoMock;
    @Mock
    private QueryBuilder<OrmInsight, Integer> queryBuilderMock;
    @Mock
    private Where<OrmInsight, Integer> whereMock;

    private List<OrmInsight> ormInsights = new ArrayList<>();


    @Before
    public void setUp() {
        initMocks(this);
        ormDeleting = new OrmDeleting(momentDaoMock, momentDetailDaoMock, measurementDaoMock,
                measurementDetailDaoMock, synchronisationDataDaoMock, measurementGroupDetailDaoMock,
                measurementGroupsDaoMock, constentDetailsDaoMock, characteristicsesDaoMock,
                settingsDaoMock, syncDaoMock, ormInsightDaoMock, ormInsightMetadataDaoMock);
        OrmInsight expiredOrmInsight = new OrmInsight();
        expiredOrmInsight.setExpirationDate(DateTime.now().minusDays(1));
        OrmInsight veryFarFutureOrmInsight = new OrmInsight();
        veryFarFutureOrmInsight.setExpirationDate(new DateTime(2999, 1,1,0,0,0));
        ormInsights.add(expiredOrmInsight);
    }

    @Test
    public void deleteAllExpiredInsights() throws Exception {
        when(ormInsightDaoMock.queryBuilder()).thenReturn(queryBuilderMock);
        when(queryBuilderMock.where()).thenReturn(whereMock);
        when(queryBuilderMock.query()).thenReturn(ormInsights);

        ormDeleting.deleteAllExpiredInsights();

        verify(whereMock).lt(eq("expirationDate"), isA(DateTime.class));
        verify(ormInsightDaoMock, times(1)).delete(isA(OrmInsight.class));
    }
}