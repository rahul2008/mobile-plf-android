/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.tagging.SHNTagger.Tagger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNTaggerTest {

    @Mock
    private Tagger taggerOne;

    @Mock
    private Tagger taggerTwo;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenMultipleTaggerInstancesAreRegistered_whenTaggingViaSHNTagger_thenAllCallsShouldBeForwardedToTheRegisteredInstances() {
        SHNTagger.registerTagger(taggerOne);
        SHNTagger.registerTagger(taggerTwo);

        final String technicalError = "dontcare";
        final String explanation = "alsodontcare";

        SHNTagger.sendTechnicalError(technicalError, explanation);

        verify(taggerOne).sendTechnicalError(eq(technicalError), eq(explanation));
        verify(taggerTwo).sendTechnicalError(eq(technicalError), eq(explanation));
    }
}
