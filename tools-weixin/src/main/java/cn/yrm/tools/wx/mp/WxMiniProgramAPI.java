package cn.yrm.tools.wx.mp;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.vo.Result;
import cn.yrm.tools.wx.comm.util.ResultUtil;
import cn.yrm.tools.wx.mp.aes.AesException;
import cn.yrm.tools.wx.mp.aes.WXBizMsgCrypt;
import cn.yrm.tools.wx.mp.vo.CodeSession;
import cn.yrm.tools.wx.mp.vo.SubscribedMessage;
import cn.yrm.tools.wx.mp.vo.SubscribedMessageTemplate;

public class WxMiniProgramAPI {


    /**
     * 获取小程序的用户Session
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param code 用户code
     * @return
     */
    public static Result<CodeSession> code2Session(String appId, String appSecret, String code) {
        String url = StrUtil.format(UrlConstant.mp_code2session_url, appId, appSecret, code);
        String result = HttpUtil.get(url, 60000);
        return ResultUtil.checkResult(result, CodeSession.class);
    }

    /**
     * 发送小程序订阅消息
     * @param accessToken 服务Token
     * @param subscribedMessage 订阅消息体(建议使用模板类build)
     * @return 成功返回消息msgid
     */
    public static Result sendSubscribedMessage(String accessToken, SubscribedMessage subscribedMessage) {
        String url = StrUtil.format(UrlConstant.mp_subscribe_msg_url, accessToken);
        String result = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(subscribedMessage)).timeout(60000).execute().body();
        return ResultUtil.checkResult(result, SubscribedMessage.class);
    }

    /**
     * 创建消息订阅模板对象
     * @param templateId 模板ID
     * @param paramKeyStr 参数名逗号分隔
     * @return
     */
    public static SubscribedMessageTemplate createMessageTemplate(String templateId, String paramKeyStr) {
        return new SubscribedMessageTemplate(templateId, paramKeyStr);
    }

    /**
     * 获得通知消息编译
     * @param appId 应用ID
     * @param notifyToken 通知Token
     * @param encodingAesKey 加密密钥
     * @return
     */
    public static WXBizMsgCrypt getNotifyDecoder(String appId, String notifyToken, String encodingAesKey) throws AesException{
        return new WXBizMsgCrypt(notifyToken, encodingAesKey, appId);
    }
}
