package cn.yrm.tools.wx.pa.code;

/**
 * 授权范围
 *
 * @author yuanr
 * @since 2022-05-29
 */
public enum OauthScopeEnum {

    /**
     * 静默授权
     */
    BASE("snsapi_base"),

    /**
     * 获取用户信息
     */
    USER_INFO("snsapi_userinfo");

    private String value;

    OauthScopeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(String value) {
        return this.getValue().equals(value);
    }

    public static OauthScopeEnum toEnumValue(Integer value) {
        for (OauthScopeEnum theEnum: OauthScopeEnum.values()) {
            if (theEnum.getValue().equals(value)) {
                return theEnum;
            }
        }
        throw new IllegalArgumentException("枚举类型转换错误:" + value);
    }

}
