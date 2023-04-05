package cn.yrm.tools.wx.pa;

import cn.hutool.http.HttpUtil;

public interface UrlConstant {

    String pa_code2session_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code";

    String pa_authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={}&redirect_uri={}&response_type=code&scope={}&state={}";

    String pa_auth_user_info_url = "https://api.weixin.qq.com/sns/userinfo?access_token={}&openid={}&lang=zh_CN";

}
