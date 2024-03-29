package cn.yrm.tools.common.code;

/**
 * @author yuanr
 */
public enum YesNoEnum {
    // 是否
    YES(1),
    NO(0);

    private Integer value;

    YesNoEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public boolean equals(Integer value) {
        return this.getValue().equals(value);
    }

    public static YesNoEnum toEnumValue(Integer value) {
        for (YesNoEnum theEnum : YesNoEnum.values()) {
            if (theEnum.getValue().equals(value)) {
                return theEnum;
            }
        }
        throw new IllegalArgumentException("枚举类型转换错误:" + value);
    }

}
