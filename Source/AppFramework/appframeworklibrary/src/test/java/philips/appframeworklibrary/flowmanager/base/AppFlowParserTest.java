package philips.appframeworklibrary.flowmanager.base;

import android.support.annotation.NonNull;

import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import philips.appframeworklibrary.flowmanager.enums.AppFlowEnum;
import philips.appframeworklibrary.flowmanager.listeners.AppFlowJsonListener;
import philips.appframeworklibrary.flowmanager.models.AppFlow;
import philips.appframeworklibrary.flowmanager.models.AppFlowEvent;
import philips.appframeworklibrary.flowmanager.models.AppFlowState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppFlowParserTest extends TestCase {

    private AppFlowParser appFlowParser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appFlowParser = new AppFlowParser();
    }

    public void testAppFlow() throws Exception {
        AppFlowJsonListener appFlowJsonListener = mock(AppFlowJsonListener.class);
        appFlowParser.getAppFlow(null, appFlowJsonListener);
        verify(appFlowJsonListener).onError(AppFlowEnum.FILE_NOT_FOUND);
        appFlowParser = new AppFlowParser() {
            @NonNull
            @Override
            InputStream getFileInputStream(final String jsonPath) throws FileNotFoundException {
                return getMockedInputStream();
            }
        };
        appFlowParser.getAppFlow("path", appFlowJsonListener);
        verify(appFlowJsonListener).onError(AppFlowEnum.JSON_PARSE_EXCEPTION);
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