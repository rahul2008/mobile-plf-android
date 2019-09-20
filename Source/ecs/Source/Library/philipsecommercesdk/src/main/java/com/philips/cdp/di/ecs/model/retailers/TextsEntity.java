/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers;

import java.io.Serializable;
import java.util.List;

public class TextsEntity implements Serializable {

    private List<TextEntity> Text;

    public List<TextEntity> getText() {
        return Text;
    }
}
