package com.philips.platform.catk.error;

import java.util.List;

/**
 * Created by Maqsood on 10/12/17.
 */

public class ServerError {

    private List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
