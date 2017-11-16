package com.github.ocraft.s2client.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public final class GameVersion implements Serializable {

    private static final long serialVersionUID = 3017683212680218667L;

    private final int baseVersion;
    private final String dataHash;
    private final String fixedHash;
    private final String label;
    private final String replayHash;
    private final int version;

    @JsonCreator
    public GameVersion(
            @JsonProperty("base-version") int baseVersion,
            @JsonProperty("data-hash") String dataHash,
            @JsonProperty("fixed-hash") String fixedHash,
            @JsonProperty("label") String label,
            @JsonProperty("replay-hash") String replayHash,
            @JsonProperty("version") int version) {
        this.baseVersion = baseVersion;
        this.dataHash = dataHash;
        this.fixedHash = fixedHash;
        this.label = label;
        this.replayHash = replayHash;
        this.version = version;
    }

    public int getBaseVersion() {
        return baseVersion;
    }

    public String getDataHash() {
        return dataHash;
    }

    public String getFixedHash() {
        return fixedHash;
    }

    public String getLabel() {
        return label;
    }

    public String getReplayHash() {
        return replayHash;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameVersion that = (GameVersion) o;

        return baseVersion == that.baseVersion &&
                version == that.version &&
                dataHash.equals(that.dataHash) &&
                fixedHash.equals(that.fixedHash) &&
                label.equals(that.label) &&
                replayHash.equals(that.replayHash);
    }

    @Override
    public int hashCode() {
        int result = baseVersion;
        result = 31 * result + dataHash.hashCode();
        result = 31 * result + fixedHash.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + replayHash.hashCode();
        result = 31 * result + version;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
