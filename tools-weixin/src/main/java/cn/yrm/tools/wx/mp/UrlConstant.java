package cn.yrm.tools.wx.mp;

public interface UrlConstant {

    String mp_code2session_url = "https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code";

    String mp_subscribe_msg_url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token={}";
}
