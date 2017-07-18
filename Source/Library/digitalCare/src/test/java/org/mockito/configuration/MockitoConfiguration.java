package org.mockito.configuration;

/**
 * Created by philips on 7/18/17.
 */

public class MockitoConfiguration extends DefaultMockitoConfiguration {

    @Override
    public boolean enableClassCache() {
        return false;
    }

}
