package com.philips.platform.csw.permission;

import dagger.Component;

@Component(modules = PermissionModule.class)
public interface PermissionComponent {
    PermissionPresenter presenter();
}
