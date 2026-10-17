package com.exammanagement.ui;

import java.awt.Color;

import javax.swing.UIManager;

public final class UiTheme {

    public static final Color BACKGROUND = new Color(248, 243, 235);
    public static final Color SURFACE = new Color(255, 251, 245);
    public static final Color INPUT_BACKGROUND = new Color(255, 253, 249);
    public static final Color BORDER = new Color(221, 203, 181);
    public static final Color TEXT = new Color(61, 43, 31);
    public static final Color MUTED_TEXT = new Color(128, 108, 88);
    public static final Color PLACEHOLDER = new Color(161, 142, 124);
    public static final Color ACCENT = new Color(139, 94, 60);
    public static final Color ACCENT_HOVER = new Color(112, 71, 47);
    public static final Color ACCENT_SOFT = new Color(233, 215, 194);
    public static final Color ON_ACCENT = new Color(255, 249, 240);
    public static final Color SUCCESS = new Color(85, 115, 74);
    public static final Color ERROR = new Color(168, 72, 61);
    public static final Color WARNING = new Color(171, 105, 43);
    public static final Color DISABLED = new Color(184, 170, 154);
    public static final Color GOLD = new Color(177, 119, 39);
    public static final Color SILVER = new Color(111, 115, 117);
    public static final Color BRONZE = new Color(157, 91, 49);

    private UiTheme() {
    }

    public static void apply() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("Button.background", ACCENT);
        UIManager.put("Button.foreground", ON_ACCENT);
        UIManager.put("TextField.background", INPUT_BACKGROUND);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextArea.background", INPUT_BACKGROUND);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("ComboBox.background", INPUT_BACKGROUND);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ScrollPane.background", SURFACE);
        UIManager.put("RadioButton.background", SURFACE);
        UIManager.put("RadioButton.foreground", TEXT);
    }
}
