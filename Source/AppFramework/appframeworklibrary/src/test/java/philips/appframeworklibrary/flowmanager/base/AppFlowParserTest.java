package philips.appframeworklibrary.flowmanager.base;

import android.support.annotation.NonNull;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import philips.appframeworklibrary.flowmanager.exceptions.JsonFileNotFoundException;
import philips.appframeworklibrary.flowmanager.exceptions.JsonStructureException;
import philips.appframeworklibrary.flowmanager.models.AppFlow;
import philips.appframeworklibrary.flowmanager.models.AppFlowEvent;
import philips.appframeworklibrary.flowmanager.models.AppFlowState;

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
            assertTrue(e.getMessage().equals("No Json file found"));
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
            assertTrue(e.getMessage().equals("Error in Json Structure"));
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
            e.printStackTrace();
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