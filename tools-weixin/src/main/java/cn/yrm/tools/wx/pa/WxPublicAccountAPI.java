package cn.yrm.tools.wx.pa;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.vo.Result;
import cn.yrm.tools.wx.comm.util.ResultUtil;
import cn.yrm.tools.wx.pa.code.OauthScopeEnum;
import cn.yrm.tools.wx.pa.vo.UserAccessToken;
import cn.yrm.tools.wx.pa.vo.UserAuthInfo;

public class WxPublicAccountAPI {


    /**
     * 获取小程序的用户Session
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param code 用户code
     * @return
     */
    public static Result<UserAccessToken> code2Session(String appId, String appSecret, String code) {
        String url = StrUtil.format(UrlConstant.pa_code2session_url, appId, appSecret, code);
        String result = HttpUtil.get(url, 60000);
        return ResultUtil.checkResult(result, UserAccessToken.class);
    }

    /**
     * 获取网页授权跳转地址 服务号且已认证 静默跳转
     * @param appId 应用ID
     * @param redirectUri 跳转地址 限80端口
     * @param state 回调附加参数
     * @return
     */
    public static String getAuthBaseUrl(String appId, String redirectUri, String state) {
        String url = StrUtil.format(UrlConstant.pa_authorize_url, appId, URLUtil.encodeAll(redirectUri),
                OauthScopeEnum.BASE.getValue(), state);
        return url;
    }

    /**
     * 获取网页授权跳转地址 服务号且已认证 需用户授权
     * @param appId 应用ID
     * @param redirectUri 跳转地址 限80端口
     * @param state 回调附加参数
     * @return
     */
    public static String getAuthWithInfoUrl(String appId, String redirectUri, String state) {
        String url = StrUtil.format(UrlConstant.pa_authorize_url, appId, URLUtil.encodeAll(redirectUri),
                OauthScopeEnum.USER_INFO.getValue(), state);
        return url;
    }

    /**
     * 获取用户授权信息
     * @param accessToken 服务端AccessToken
     * @param openId 用户OpenId
     * @return
     */
    public static Result<UserAuthInfo> getAuthUserInfo(String accessToken, String openId) {
        String url = StrUtil.format(UrlConstant.pa_auth_user_info_url, accessToken, openId);
        String result = HttpUtil.get(url, 60000);
        return ResultUtil.checkResult(result, UserAuthInfo.class);
    }


    public static void main(String[] args) {
//        String appId = "wx304d85c8e1eecbe2";
//        String appSecret = "c997dffd7455b8b1336fd2a0199dd29b";
//        String code = "071ea50w3SdfHY2lHc1w3CcAo64ea50I";
//        Result<AccessToken> res2 = WxServerAPI.getAccessToken(appId, appSecret);
//        System.out.println(JSONUtil.toJsonStr(res2));
//        String accessToken = res2.getData().getAccess_token();
        String accessToken = "67_S_N-t8kCw_TRDByRouW7sAAwxhLcIlfbWPnAeL_tg47cemoqcwxjij90uNCkv88bhUeoWKEd5sn8xO4tnaECznia_z3x-p6FSU5g3yzTyWk";
        String openId = "oNoFR0SxAoSqlJp7uruK4fVROd-0";
        Result result = WxPublicAccountAPI.getAuthUserInfo(accessToken, openId);
        System.out.println(JSONUtil.toJsonStr(result));
    }

}
