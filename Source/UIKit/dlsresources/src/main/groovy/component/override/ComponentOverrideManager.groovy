package component.override

import com.philips.uid.BrushParser

public class ComponentOverrideManager {
    private static final String DIALOG_ALERT_DEFAULT_BODY = "uidDialogAlertDefaultBody"
    private static final String DIALOG_ALERT_DEFAULT_CLOSE = "uidDialogAlertDefaultClose"
    private static final String DIALOG_ALERT_DEFAULT_TITLE = "uidDialogAlertDefaultTitle"

    ArrayList<ComponentTROverride> overrideList = new ArrayList<>()
    static ComponentOverrideManager manager = new ComponentOverrideManager();

    private ComponentOverrideManager() {
    }

    public static ComponentOverrideManager getManagerInstance() {
        if (manager == null) {
            manager = new ComponentOverrideManager();
        }
        return manager;
    }

    public void updateOverrideList(String componentAttributeName, String brushName) {
        addDialogAlertOverride(overrideList, BrushParser.getAttributeName(componentAttributeName), BrushParser.getAttributeName(brushName))
//        if (overrideList.size() > 0) {
//            println(overrideList)
//        }
    }

    public String getOverridenTonalRange(String componentAttributeName, String tonalRange) {
        for(ComponentTROverride componentTROverride: overrideList) {
            if(componentTROverride.name ==componentAttributeName)
               return componentTROverride.getOverridenTonalRange(tonalRange)
        }
        throw new RuntimeException("overridesTR must be checked before calling getOverridenTonalRange")
    }

    public boolean overridesTR(String componentAttributeName) {
        for(ComponentTROverride componentTROverride: overrideList) {
            if(componentTROverride.name ==componentAttributeName)
                return true;
        }
        return false;
    }

    public void addDialogAlertOverride(List exceptionList, String controlAttributedName, String brushName) {
        if (isDialogAlertOverrideProperties(controlAttributedName)){
            ComponentTROverride componentOverride = new ComponentTROverride(controlAttributedName, brushName);
            if (!exceptionList.contains(componentOverride)) {
                exceptionList.add(componentOverride);
            }
        }
    }

    private boolean isDialogAlertOverrideProperties(String controlAttributedName) {
        return controlAttributedName.startsWith(DIALOG_ALERT_DEFAULT_BODY) || controlAttributedName.startsWith(DIALOG_ALERT_DEFAULT_CLOSE) || controlAttributedName.startsWith(DIALOG_ALERT_DEFAULT_TITLE)
    }
}