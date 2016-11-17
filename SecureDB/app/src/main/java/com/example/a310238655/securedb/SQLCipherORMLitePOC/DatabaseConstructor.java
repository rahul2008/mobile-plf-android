package com.example.a310238655.securedb.SQLCipherORMLitePOC;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by 310238655 on 11/10/2016.
 */

/**
 * Annotation to remove Android studio warning about unused constructor for DB classes
 */

@Target(CONSTRUCTOR)
@Retention(SOURCE)
public @interface DatabaseConstructor {
}

