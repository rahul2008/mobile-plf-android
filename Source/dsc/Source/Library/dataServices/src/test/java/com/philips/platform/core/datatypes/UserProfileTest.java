package com.philips.platform.core.datatypes;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileTest {

    UserProfile userProfile;

    @Before
    public void setUp() {
        userProfile = new UserProfile("John", "Doa", "johndao@gmail.com", "fdsf3243211sdfs2214");
    }

    @Test
    public void ShouldReturnLastName_WhenAsked() {
        assertThat(userProfile.getLastName()).isEqualToIgnoringCase("Doa");
    }

    @Test
    public void ShouldReturnFirstName_WhenAsked() {
        assertThat(userProfile.getFirstName()).isEqualToIgnoringCase("John");
    }

    @Test
    public void ShouldReturnEmail_WhenAsked() {
        assertThat(userProfile.getEmail()).isEqualToIgnoringCase("johndao@gmail.com");
    }

    @Test
    public void ShouldReturnGuid_WhenAsked() {
        assertThat(userProfile.getGUid()).isEqualToIgnoringCase("fdsf3243211sdfs2214");
    }

}