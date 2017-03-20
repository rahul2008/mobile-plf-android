
package ${packageName};


import android.content.Context;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class ${escapeXmlString(appTitle)}uAppSettings extends UappSettings {

	public ${escapeXmlString(appTitle)}uAppSettings(final Context applicationContext) {
        super(applicationContext);
	}
<#include "../../../../common/jni_code_snippet.java.ftl">
}
