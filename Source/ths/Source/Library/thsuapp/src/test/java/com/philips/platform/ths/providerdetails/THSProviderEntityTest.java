package com.philips.platform.ths.providerdetails;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class THSProviderEntityTest {
    THSProviderEntity thsProviderEntity;

    @Mock
    Parcel parcel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsProviderEntity = new THSProviderEntity();
    }

    @Test
    public void describeContents() throws Exception {
        int i = thsProviderEntity.describeContents();
        assert i ==0;
    }


    @Test
    public void writeToParcel() throws Exception {
        thsProviderEntity.writeToParcel(parcel,0);
    }

    @Test
    public void createFromParcel(){
        THSProviderEntity fromParcel = THSProviderEntity.CREATOR.createFromParcel(parcel);
        assertThat(fromParcel).isInstanceOf(THSProviderEntity.class);
    }

    @Test
    public void createFromnewArray(){
        THSProviderEntity[] thsProviderEntities = THSProviderEntity.CREATOR.newArray(1);
        assertThat(thsProviderEntities).isInstanceOf(THSProviderEntity[].class);
    }
}