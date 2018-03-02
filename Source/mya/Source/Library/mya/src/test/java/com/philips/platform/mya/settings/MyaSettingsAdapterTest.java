package com.philips.platform.mya.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaSettingsAdapterTest {
    private MyaSettingsAdapter myaSettingsAdapter;

    private final static int DOUBLE_VIEW = 0;
    private final static int SINGLE_VIEW = 1;

    @Mock
    Context contextMock;

    @Mock
    LayoutInflater layoutInflaterMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    View viewMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        LinkedHashMap<String, SettingsModel> map = new LinkedHashMap<>();
        addData(map);
        myaSettingsAdapter = new MyaSettingsAdapter(contextMock, map);
        assertTrue(myaSettingsAdapter.getItemCount() == 2);
        View.OnClickListener onClickListener = mock(View.OnClickListener.class);
        myaSettingsAdapter.setOnClickListener(onClickListener);
        assertNotNull(myaSettingsAdapter.getSettingsList());
    }

    private void addData(LinkedHashMap<String, SettingsModel> map) {
        SettingsModel countrySettingsModel = new SettingsModel();
        countrySettingsModel.setItemCount(2);
        countrySettingsModel.setFirstItem("some_first_item");
        countrySettingsModel.setSecondItem("IN");
        map.put("MYA_Country", countrySettingsModel);
        map.put("Mya_Privacy_Settings", new SettingsModel());
    }

    @Test
    public void testOnCreateViewForSingleView() throws Exception {
        View view = mock(View.class);
        Label label = mock(Label.class);
        Label label2 = mock(Label.class);
        when(view.findViewById(R.id.item_title)).thenReturn(label);
        when(view.findViewById(R.id.second_item)).thenReturn(label2);
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        when(layoutInflaterMock.inflate(R.layout.mya_single_item_layout, viewGroupMock, false)).thenReturn(view);
        MyaSettingsAdapter.SettingsViewHolder settingsViewHolder = myaSettingsAdapter.onCreateViewHolder(viewGroupMock, SINGLE_VIEW);
        myaSettingsAdapter.onBindViewHolder(settingsViewHolder, 0);
        verify(label).setText("some_first_item");
        verify(label2).setText("IN");

        myaSettingsAdapter.onBindViewHolder(settingsViewHolder, 1);
        verify(label).setText("Mya_Privacy_Settings");
        assertNotNull(myaSettingsAdapter);
    }

    @Test(expected = NullPointerException.class)
    public void testOnCreateViewForDoubleView() throws Exception {
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        myaSettingsAdapter.onCreateViewHolder(viewGroupMock, DOUBLE_VIEW);
        assertNotNull(myaSettingsAdapter);
    }

    @Test
    public void shouldNotNull_getItemCount() {
        assertNotNull(myaSettingsAdapter.getItemCount());
    }


}