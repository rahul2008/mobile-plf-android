package com.philips.platform.appframework.flowmanager.base;

import androidx.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonStructureException;
import com.philips.platform.appframework.flowmanager.models.AppFlow;
import com.philips.platform.appframework.flowmanager.models.AppFlowEvent;
import com.philips.platform.appframework.flowmanager.models.AppFlowState;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppFlowParserTest extends TestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private AppFlowParser appFlowParser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appFlowParser = new AppFlowParser();
    }

    public void testAppFlow() throws Exception {

        exception.expect(JsonFileNotFoundException.class);
        try {
            appFlowParser.getAppFlow(null);
        } catch (JsonFileNotFoundException e) {
            assertTrue(e.getMessage().equals("There is no Json in the given path"));
        }
        try {
            appFlowParser.getAppFlow("data");
        } catch (JsonFileNotFoundException e) {
            assertTrue(e.getMessage().equals("There is no Json in the given path"));
        }

        appFlowParser = new AppFlowParser() {
            @NonNull
            @Override
            InputStream getFileInputStream(final String jsonPath) throws FileNotFoundException {
                return getMockedInputStream();
            }
        };
        exception.expect(JsonStructureException.class);
        try {
            appFlowParser.getAppFlow("path");
        } catch (JsonStructureException e) {
            assertTrue(e.getMessage().equals("The Json structure is wrong"));
        }
    }

    public void testAppFlowWithResID() {
        exception.expect(JsonFileNotFoundException.class);
        try {
            appFlowParser.getAppFlow(0);
        } catch (JsonFileNotFoundException e) {
            assertTrue(e.getMessage().equals("There is no Json in the given path"));
        }
    }

    @SuppressWarnings("deprecation")
    private InputStream getMockedInputStream() {
        String testJsonData = "ghfhfhf";
        return new StringBufferInputStream(testJsonData);
    }

    public void testIsEmpty() {
        assertTrue(appFlowParser.isEmpty(""));
        assertTrue(appFlowParser.isEmpty(null));
        assertFalse(appFlowParser.isEmpty("test"));
        try {
            assertTrue(appFlowParser.getFileInputStream("test") != null);
        } catch (FileNotFoundException e) {
            assertEquals("test (No such file or directory)",e.getMessage());
        }
    }

    public void testAppFlowMap() throws Exception {
        List<AppFlowState> appFlowStates = new ArrayList<>();
        appFlowStates.add(new AppFlowState());
        appFlowStates.add(new AppFlowState());
        appFlowStates.add(new AppFlowState());
        appFlowStates.add(new AppFlowState());
        AppFlow appFlow = mock(AppFlow.class);
        when(appFlow.getStates()).thenReturn(appFlowStates);
        final Map<String, List<AppFlowEvent>> appFlowMap = appFlowParser.getAppFlowMap(appFlow);
        assertTrue(appFlowMap != null);
        assertTrue(appFlowMap.size() == 1);
    }
}