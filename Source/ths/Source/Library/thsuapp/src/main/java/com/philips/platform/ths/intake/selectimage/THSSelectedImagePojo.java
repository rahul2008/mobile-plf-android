/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class THSSelectedImagePojo implements Parcelable{
    private String title, description, path;
    private Calendar datetime;
    private long datetimeLong;
    private boolean isUploaded;
    protected static SimpleDateFormat df = new SimpleDateFormat("MMMM d, yy  h:mm");


    public THSSelectedImagePojo(){

    }
    public THSSelectedImagePojo(Parcel in) {
        title = in.readString();
        description = in.readString();
        path = in.readString();
        datetimeLong = in.readLong();
        isUploaded = in.readByte() != 0;
    }

    public static final Creator<THSSelectedImagePojo> CREATOR = new Creator<THSSelectedImagePojo>() {
        @Override
        public THSSelectedImagePojo createFromParcel(Parcel in) {
            return new THSSelectedImagePojo(in);
        }

        @Override
        public THSSelectedImagePojo[] newArray(int size) {
            return new THSSelectedImagePojo[size];
        }
    };

    /**
     *
     * @return
     */
    public boolean isUploaded() {
        return isUploaded;
    }

    /**
     *
     * @param uploaded
     */
    public void setIsUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    /**
     * Gets title.
     *
     * @return Value of title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets datetime.
     *
     * @return Value of datetime.
     */
    public Calendar getDatetime() {
        return datetime;
    }

    /**
     * Sets new datetimeLong.
     *
     * @param datetimeLong New value of datetimeLong.
     */
    public void setDatetime(long datetimeLong) {
        this.datetimeLong = datetimeLong;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datetimeLong);
        this.datetime = cal;
    }

    /**
     * Sets new datetime.
     *
     * @param datetime New value of datetime.
     */
    public void setDatetime(Calendar datetime) {
        this.datetime = datetime;
    }

    /**
     * Gets description.
     *
     * @return Value of description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets new title.
     *
     * @param title New value of title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets datetimeLong.
     *
     * @return Value of datetimeLong.
     */
    public long getDatetimeLong() {
        return datetimeLong;
    }

    /**
     * Sets new description.
     *
     * @param description New value of description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets new path.
     *
     * @param path New value of path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets path.
     *
     * @return Value of path.
     */
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Title:" + title + "   " + df.format(datetime.getTime()) +
                "\nDescription:" + description + "\nPath:" + path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(path);
        dest.writeLong(datetimeLong);
        dest.writeByte((byte) (isUploaded ? 1 : 0));
    }
}