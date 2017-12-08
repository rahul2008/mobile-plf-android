/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.characteristics;

import java.util.List;

public class UCoreUserCharacteristics {
    private List<UCoreCharacteristics> characteristics;

    public void setCharacteristics(List<UCoreCharacteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<UCoreCharacteristics> getCharacteristics() {
        return characteristics;
    }

}