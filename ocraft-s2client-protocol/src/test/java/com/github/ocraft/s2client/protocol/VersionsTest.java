package com.github.ocraft.s2client.protocol;

import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.BASE_BUILD;
import static org.assertj.core.api.Assertions.assertThat;

class VersionsTest {

    @Test
    void providesInformationAboutGameVersions() {
        assertThat(Versions.versionFor(BASE_BUILD)).isNotEmpty();
    }

}