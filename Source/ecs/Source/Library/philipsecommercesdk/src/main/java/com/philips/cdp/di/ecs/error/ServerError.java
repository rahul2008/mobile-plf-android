package com.philips.cdp.di.ecs.error;

import java.util.List;

public class ServerError {
    private List<Error> errors;

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

}
