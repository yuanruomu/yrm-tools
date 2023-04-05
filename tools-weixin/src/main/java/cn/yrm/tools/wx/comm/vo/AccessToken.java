package cn.yrm.tools.wx.comm.vo;

import lombok.Data;

/**
 * @author yuanr
 */
@Data
public class AccessToken {

    /**
     * 获取到的凭证
     */
    private String access_token;
    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;
}
