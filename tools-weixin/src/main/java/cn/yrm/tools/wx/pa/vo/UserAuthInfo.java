package cn.yrm.tools.wx.pa.vo;

import lombok.Data;

@Data
public class UserAuthInfo {


    /**
     * 用户的标识，对当前公众号唯一
     */
    private String openid;

    /**
     * 用户微信昵称
     */
    private String nickname;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer sex;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    private String language;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    private String unionid;
    /**
     * 普通用户个人资料填写的城市
     */
    private String city;
    /**
     * 用户个人资料填写的省份
     */
    private String province;
    /**
     * 国家，如中国为CN
     */
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效
     */
    private String headimgurl;
    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    private String privilege;
}
