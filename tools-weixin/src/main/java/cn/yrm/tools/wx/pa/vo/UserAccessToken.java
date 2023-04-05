package cn.yrm.tools.wx.pa.vo;

import lombok.Data;

/**
 * @author yuanr
 */
@Data
public class UserAccessToken {

    /**
     * 获取到的凭证
     */
    private String access_token;
    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;
    /**
     * 用户刷新access_token
     */
    private String refresh_token;
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户授权的作用域
     */
    private String scope;
}
