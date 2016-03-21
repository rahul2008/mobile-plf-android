package com.philips.pins.shinelib.datatypes;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SHNLogItemTest {

    private Set<SHNDataType> dataTypes;
    private Map<SHNDataType, SHNData> dataByDataTypeMap;

    @Before
    public void setUp() throws Exception {
        dataTypes = new HashSet<>();
        dataTypes.add(SHNDataType.EnergyExpenditure);
        dataTypes.add(SHNDataType.ActiveEnergyExpenditure);
        dataByDataTypeMap = new HashMap<>();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canNotModifyContainedDataTypes() {
        SHNLogItem shnLogItem = new SHNLogItem(new Date(), dataTypes, dataByDataTypeMap);

        shnLogItem.getContainedDataTypes().remove(SHNDataType.EnergyExpenditure);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void canNotModifyDataMap() {
        SHNLogItem shnLogItem = new SHNLogItem(new Date(), dataTypes, dataByDataTypeMap);

        shnLogItem.getDataByDataTypeMap().clear();
    }
}