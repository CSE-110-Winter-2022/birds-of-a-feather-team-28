package com.example.bof_group_28.utility.enums;

public enum QuarterName {

    EMPTY(""),
    FALL("Fall"),
    WINTER("Winter"),
    SPRING("Spring"),
    SUMMER_SESSION_ONE("Summer1"),
    SUMMER_SESSION_TWO("Summer2"),
    SPECIAL_SUMMER_SESSION("SummerSpec");

    private String text;

    QuarterName(String text) {
        this.text = text;
    }

    String getText() {
        return text;
    }

    /**
     * Get all types of enums as string array format
     * @return types
     */
    public static String[] types() {
        String[] types = new String[QuarterName.values().length];
        for (int i = 0; i < QuarterName.values().length; i++) {
            types[i] = QuarterName.values()[i].text;
        }
        return types;
    }

}
