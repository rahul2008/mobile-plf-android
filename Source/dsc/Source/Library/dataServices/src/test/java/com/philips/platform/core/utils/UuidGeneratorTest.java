package com.philips.platform.core.utils;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UuidGeneratorTest {
    @Test
    public void ShouldGenerateUUID_WhenAsked() throws Exception {
        UuidGenerator generator = new UuidGenerator();

        final UUID uuid = UUID.fromString(generator.generateRandomUUID());

        assertThat(uuid).isNotNull();
        //4 - is random UUID
        assertThat(uuid.version()).isEqualTo(4);
    }
}