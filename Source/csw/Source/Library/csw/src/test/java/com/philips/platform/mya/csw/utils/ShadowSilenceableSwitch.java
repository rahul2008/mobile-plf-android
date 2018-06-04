package com.philips.platform.mya.csw.utils;

import com.philips.platform.mya.csw.permission.uielement.SilenceableSwitch;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

@Implements(SilenceableSwitch.class)
public class ShadowSilenceableSwitch extends ShadowView {
}
