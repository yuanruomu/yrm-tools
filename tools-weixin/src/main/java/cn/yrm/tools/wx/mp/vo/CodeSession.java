package cn.yrm.tools.wx.mp.vo;

import lombok.Data;

/**
 * @author yuanr
 */
@Data
public class CodeSession {

    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 会话密钥
     */
    private String session_key;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionid;
}
