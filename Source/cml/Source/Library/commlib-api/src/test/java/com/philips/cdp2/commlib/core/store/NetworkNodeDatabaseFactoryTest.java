package com.philips.cdp2.commlib.core.store;

import android.content.Context;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NetworkNodeDatabaseFactory.class)
public class NetworkNodeDatabaseFactoryTest {

    @Mock
    private Context mockContext;

    @Mock
    private File mockFile;

    @Mock
    private RuntimeConfiguration runtimeConfiguration;

    @Mock
    private AppInfraInterface mockAppInfraInterface;

    @Mock
    private SecureNetworkNodeDatabaseHelper mockSecure;

    @Mock
    private NonSecureNetworkNodeDatabaseHelper mockOpen;

    @Mock
    private NetworkNodeDatabase nonSecureDB;

    @Mock
    private NetworkNodeDatabase secureDB;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        whenNew(SecureNetworkNodeDatabaseHelper.class).withAnyArguments().thenReturn(mockSecure);
        whenNew(NonSecureNetworkNodeDatabaseHelper.class).withAnyArguments().thenReturn(mockOpen);

        whenNew(NetworkNodeDatabase.class).withArguments(mockOpen).thenReturn(nonSecureDB);
        whenNew(NetworkNodeDatabase.class).withArguments(mockSecure).thenReturn(secureDB);

        when(runtimeConfiguration.getContext()).thenReturn(mockContext);
        when(mockContext.getDatabasePath(anyString())).thenReturn(mockFile);

        ContextProvider.setTestingContext(mockContext);
    }

    @Test
    public void whenRuntimeConfigurationIsNotProvided_thenANonsecureDatabaseIsCreated() {
        final NetworkNodeDatabase database = NetworkNodeDatabaseFactory.create(null);

        assertEquals(nonSecureDB, database);
    }

    @Test
    public void whenAppInfraInterfaceIsNotProvided_thenANonsecureDatabaseIsCreated() {
        final NetworkNodeDatabase database = NetworkNodeDatabaseFactory.create(runtimeConfiguration);

        assertEquals(nonSecureDB, database);
    }

    @Test
    public void whenAppInfraInterfaceIsProvided_thenASecureDatabaseIsCreated() {
        when(runtimeConfiguration.getAppInfraInterface()).thenReturn(mockAppInfraInterface);

        final NetworkNodeDatabase database = NetworkNodeDatabaseFactory.create(runtimeConfiguration);

        assertEquals(secureDB, database);
    }

    @Test
    public void whenOldDataBaseFileExist_thenAllNodesAreMoved() {
        when(runtimeConfiguration.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockFile.exists()).thenReturn(true);
        NetworkNode node1 = mock(NetworkNode.class);
        NetworkNode node2 = mock(NetworkNode.class);
        when(nonSecureDB.getAll()).thenReturn(Arrays.asList(node1, node2));

        NetworkNodeDatabaseFactory.create(runtimeConfiguration);

        verify(secureDB).save(node1);
        verify(secureDB).save(node2);
    }

    @Test
    public void givenNonSecureDbExists_whenSecureDbCannotBeCreated_thenNonSecureDatabaseIsCreatedWithoutMigration() throws Exception {
        when(runtimeConfiguration.getAppInfraInterface()).thenReturn(null);
        when(mockFile.exists()).thenReturn(true);
        NetworkNode node1 = mock(NetworkNode.class);
        NetworkNode node2 = mock(NetworkNode.class);
        when(nonSecureDB.getAll()).thenReturn(Arrays.asList(node1, node2));

        final NetworkNodeDatabase database = NetworkNodeDatabaseFactory.create(runtimeConfiguration);

        assertEquals(nonSecureDB, database);
        verify(nonSecureDB, never()).save(any(NetworkNode.class));
    }
}