package com.philips.platform.datasync.insights;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.testing.verticals.table.TestInsight;
import com.philips.testing.verticals.table.TestInsightMetaData;
import com.philips.testing.verticals.table.TestSynchronisationData;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class InsightSegregatorTest {
    private InsightSegregator mInsightSegregator;

    @Mock
    AppComponent mAppComponent;
    @Mock
    DBUpdatingInterface mDBUpdatingInterface;
    @Mock
    DBSavingInterface mDBSavingInterface;
    @Mock
    DBFetchingInterface mDBFetchingInterface;
    @Mock
    DBDeletingInterface mDBDeletingInterface;
    @Mock
    private TestInsight mTestInsight;
    @Mock
    DBRequestListener mDBRequestListener;

    @Mock
    BaseAppDataCreator dataCreatorMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mInsightSegregator = new InsightSegregator();
        mInsightSegregator.mDBUpdatingInterface = mDBUpdatingInterface;
        mInsightSegregator.mDBFetchingInterface = mDBFetchingInterface;
        mInsightSegregator.mDBDeletingInterface = mDBDeletingInterface;
        mInsightSegregator.mDBSavingInterface = mDBSavingInterface;
        mInsightSegregator.mBaseAppDataCreator=dataCreatorMock;
    }

    @Test
    public void processInsightTest() throws SQLException {
        Insight appInsight = new TestInsight();

        SynchronisationData synchronisationData = new TestSynchronisationData("aefe5623-a7ac-4b4a-b789-bdeaf23add9f", false, new DateTime(), 1);
        appInsight.setSynchronisationData(synchronisationData);
        appInsight.getSynchronisationData().setGuid("aefe5623-a7ac-4b4a-b789-bdeaf23add9f");
        appInsight.getSynchronisationData().setLastModified(DateTime.parse("2017-03-21T10:19:51.706Z", ISODateTimeFormat.dateTime()));
        appInsight.getSynchronisationData().setInactive(false);
        appInsight.getSynchronisationData().setVersion(1);
        appInsight.setRuleId("ruleID");
        appInsight.setSubjectId("subjectID");
        appInsight.setMomentId("momentID");
        appInsight.setType("type");
        appInsight.setTimeStamp("2018-01-01T07:07:14.000Z");
        appInsight.setTitle("title");
        appInsight.setProgram_maxVersion(2);
        appInsight.setProgram_minVersion(1);

        InsightMetadata insightMetadata = new TestInsightMetaData("avg", "200", (TestInsight) appInsight);
        appInsight.addInsightMetaData(insightMetadata);

        List<Insight> insightList = new ArrayList<>();
        insightList.add(appInsight);

        mInsightSegregator.processInsights(insightList, mDBRequestListener);
        verify(mDBSavingInterface).saveInsights(insightList, mDBRequestListener);
    }

    @Test
    public void putInsightForSYncTest() throws SQLException {
        List<Insight> insightList = new ArrayList<>();
        TestInsight insight = new TestInsight();
        insight.getSynchronisationData().setGuid("aefe5623-a7ac-4b4a-b789-bdeaf23add9f");
        insight.getSynchronisationData().setLastModified(DateTime.parse("2017-03-21T10:19:51.706Z", ISODateTimeFormat.dateTime()));
        insight.getSynchronisationData().setInactive(false);
        insight.getSynchronisationData().setVersion(2);
        insight.setRuleId("ruleID");
        insight.setSubjectId("subjectID");
        insight.setMomentId("momentID");
        insight.setType("type");
        insight.setTimeStamp("2018-01-01T07:07:14.000Z");
        insight.setTitle("title");
        insight.setProgram_maxVersion(2);
        insight.setProgram_minVersion(1);
        insightList.add(insight);

        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Insight.class, insightList);
        mInsightSegregator.putInsightForSync(dataToSync);
        verify(mDBFetchingInterface).fetchNonSynchronizedInsights();
    }

}