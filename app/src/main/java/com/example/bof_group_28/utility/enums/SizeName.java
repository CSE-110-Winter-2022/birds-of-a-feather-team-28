package com.example.bof_group_28.utility.enums;

public enum SizeName {

    EMPTY(""),
    TINY("Tiny (<40)"),
    SMALL("Small (40-75)"),
    MEDIUM("Medium (75-150)"),
    LARGE("Large (150-250)"),
    HUGE("Huge (250-400)"),
    GIGANTIC("Gigantic (400+)");

    private String text;

    SizeName(String text) {
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
        String[] types = new String[SizeName.values().length];
        for (int i = 0; i < SizeName.values().length; i++) {
            types[i] = SizeName.values()[i].text;
        }
        return types;
    }

}
