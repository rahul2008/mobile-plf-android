package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SubjectProfileListener;

public class CreateSubjectProfileRequestEvent extends Event {
    private String firstName;
    private String dateOfBirth;
    private String gender;
    private double weight;
    private String creationDate;
    private SubjectProfileListener subjectProfileListener;

    public CreateSubjectProfileRequestEvent(String firstName, String dateOfBirth, String gender,
                                            double weight, String creationDate, SubjectProfileListener subjectProfileListener){
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.weight = weight;
        this.creationDate = creationDate;
        this.subjectProfileListener = subjectProfileListener;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public SubjectProfileListener getSubjectProfileListener() {
        return subjectProfileListener;
    }

    public void setSubjectProfileListener(SubjectProfileListener subjectProfileListener) {
        this.subjectProfileListener = subjectProfileListener;
    }
}
