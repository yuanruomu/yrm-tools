package cn.yrm.tools.wx.mp.code;

/**
 * 小程序状态
 *
 * @author yuanr
 * @since 2022-05-29
 */
public enum ProgramStateEnum {

    /**
     * 开发版
     */
    DEVELOP("developer"),

    /**
     * 体验版
     */
    TRIAL("trial"),

    /**
     * 正式版
     */
    PUBLISH("formal");

    private String value;

    ProgramStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(String value) {
        return this.getValue().equals(value);
    }

    public static ProgramStateEnum toEnumValue(Integer value) {
        for (ProgramStateEnum theEnum: ProgramStateEnum.values()) {
            if (theEnum.getValue().equals(value)) {
                return theEnum;
            }
        }
        throw new IllegalArgumentException("枚举类型转换错误:" + value);
    }

}
