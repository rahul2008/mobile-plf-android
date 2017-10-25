package com.philips.platform.csw.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={CswModule.class})
@Singleton
public interface CswComponent {
    Context context();
}