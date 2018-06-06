package com.philips.platform.csw.dialogs;

import java.util.Objects;

public class ConfirmDialogTextResources {
    private final int titleRes;
    private final int descriptionRes;
    private final int okButtonRes;
    private final int cancelButtonRes;

    public ConfirmDialogTextResources(final int titleRes, final int descriptionRes, final int okButtonRes, final int cancelButtonRes) {
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
        this.okButtonRes = okButtonRes;
        this.cancelButtonRes = cancelButtonRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public int getOkButtonRes() {
        return okButtonRes;
    }

    public int getCancelButtonRes() {
        return cancelButtonRes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ConfirmDialogTextResources that = (ConfirmDialogTextResources) o;
        return titleRes == that.titleRes &&
                descriptionRes == that.descriptionRes &&
                okButtonRes == that.okButtonRes &&
                cancelButtonRes == that.cancelButtonRes;
    }

    @Override
    public int hashCode() {

        return Objects.hash(titleRes, descriptionRes, okButtonRes, cancelButtonRes);
    }
}
