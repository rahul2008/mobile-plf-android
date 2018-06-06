package com.philips.platform.csw.utils;

import com.philips.platform.csw.permission.uielement.SilenceableSwitch;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

@Implements(SilenceableSwitch.class)
public class ShadowSilenceableSwitch extends ShadowView {
}
