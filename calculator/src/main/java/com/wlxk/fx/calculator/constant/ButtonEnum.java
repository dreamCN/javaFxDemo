package com.wlxk.fx.calculator.constant;

/**
 * @author: 鼠行止
 * @date: 2020/1/6
 */
public enum ButtonEnum {
    /**
     * 按钮
     */
    CE("ceButton", "", "CE"),
    C("cButton", "", "C"),
    CANCEL("cancelButton", "", "☒"),
    SIGN("signButton", "", "±"),
    EQUAL("equalButton", ".", "="),
    ADD("addButton", "+", "+"),
    SUBTRACT("subtractButton", "-", "-"),
    MULTIPLY("multiplyButton", "×", "×"),
    DIVIDE("divideButton", "÷", "÷"),
    DOT("dotButton", ".", "."),
    ZERO("zeroButton", "0", "0"),
    ONE("oneButton", "1", "1"),
    TWO("twoButton", "2", "2"),
    THREE("threeButton", "3", "3"),
    FOUR("fourButton", "4", "4"),
    FIVE("fiveButton", "5", "5"),
    SIX("sixButton", "6", "6"),
    SEVEN("sevenButton", "7", "7"),
    EIGHT("eightButton", "8", "8"),
    NINE("nineButton", "9", "9"),
    ;

    private String id;

    private String value;

    private String desc;

    ButtonEnum(String id, String value, String desc) {
        this.id = id;
        this.value = value;
        this.desc = desc;
    }

    public static ButtonEnum getEnumById(String id) {
        for (ButtonEnum buttonEnum : ButtonEnum.values()) {
            if (buttonEnum.getId().equals(id)) {
                return buttonEnum;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
