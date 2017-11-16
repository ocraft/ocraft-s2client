package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum UnitAttribute {
    LIGHT,
    ARMORED,
    BIOLOGICAL,
    MECHANICAL,
    ROBOTIC,
    PSIONIC,
    MASSIVE,
    STRUCTURE,
    HOVER,
    HEROIC,
    SUMMONED;

    public static UnitAttribute from(Data.Attribute sc2ApiAttribute) {
        require("sc2api attribute", sc2ApiAttribute);
        switch (sc2ApiAttribute) {
            case Hover:
                return HOVER;
            case Light:
                return LIGHT;
            case Heroic:
                return HEROIC;
            case Armored:
                return ARMORED;
            case Massive:
                return MASSIVE;
            case Psionic:
                return PSIONIC;
            case Robotic:
                return ROBOTIC;
            case Summoned:
                return SUMMONED;
            case Structure:
                return STRUCTURE;
            case Biological:
                return BIOLOGICAL;
            case Mechanical:
                return MECHANICAL;
            default:
                throw new AssertionError("unknown sc2api attribute: " + sc2ApiAttribute);

        }
    }
}
