
package ${packageName};


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class ${escapeXmlString(appTitle)}uAppDependencies extends UappDependencies {

	public ${escapeXmlString(appTitle)}uAppDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
	}
<#include "../../../../common/jni_code_snippet.java.ftl">
}
