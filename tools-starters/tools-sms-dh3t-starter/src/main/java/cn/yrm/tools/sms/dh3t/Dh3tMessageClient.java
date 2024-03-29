package cn.yrm.tools.sms.dh3t;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.vo.Result;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

@Slf4j
public class Dh3tMessageClient {

    private final String sendUrl = "/json/sms/Submit";
    private final String batchSendUrl = "/json/sms/BatchSubmit";
    private boolean isMock;
    private String host;
    private String sign;
    private String account;
    private String password;

    public Dh3tMessageClient(String host, String account, String password, String sign, boolean isMock) {
        this.host = host;
        this.isMock = isMock;
        this.account = account;
        this.sign = "【" + sign + "】";
        this.password = SecureUtil.md5(password);
    }

    public Result sendSms(String phone, String content) {
        if (isMock) {
            return Result.success();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phones", phone);
            jsonObject.put("content", content);
            jsonObject.put("sign", sign);
            jsonObject.put("account", account);
            jsonObject.put("password", password);
            String result = HttpUtil.post(this.host + this.sendUrl, jsonObject);
            JSONObject resObj = JSONUtil.parseObj(result);
            if (resObj.getInt("result") == 0) {
                return Result.success();
            } else {
                return Result.error(resObj.getStr("desc"));
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
