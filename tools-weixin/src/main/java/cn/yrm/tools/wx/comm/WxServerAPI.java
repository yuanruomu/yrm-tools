package cn.yrm.tools.wx.comm;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.yrm.tools.common.vo.Result;
import cn.yrm.tools.wx.comm.util.ResultUtil;
import cn.yrm.tools.wx.comm.vo.AccessToken;

public class WxServerAPI {

    /**
     * 获取access_token
     * @param appId
     * @param appSecret
     * @return
     */
    public static Result<AccessToken> getAccessToken(String appId, String appSecret) {
        String url = StrUtil.format(UrlConstant.access_token_url, appId, appSecret);
        String result = HttpUtil.get(url, 60000);
        return ResultUtil.checkResult(result, AccessToken.class);
    }


}
