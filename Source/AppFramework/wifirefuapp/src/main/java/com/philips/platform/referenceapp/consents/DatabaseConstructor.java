package com.philips.platform.referenceapp.consents;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target(CONSTRUCTOR)
@Retention(SOURCE)
public @interface DatabaseConstructor {
}
