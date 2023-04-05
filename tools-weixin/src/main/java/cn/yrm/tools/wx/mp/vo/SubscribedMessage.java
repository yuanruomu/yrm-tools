package cn.yrm.tools.wx.mp.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuanr
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SubscribedMessage {

    /**
     * 消息ID
     */
    private String msgid;
    /**
     * 模板编号
     */
    private String template_id;
    /**
     * 发送用户
     */
    private String touser;
    /**
     * 小程序版本
     */
    private String miniprogram_state;
    /**
     * 小程序跳转页
     */
    private String page = "index";
    /**
     * 语言
     */
    private String lang = "zh_CN";
    /**
     * 参数
     */
    private JSONObject data;
}
