package cn.yrm.tools.wx.comm.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.vo.Result;

public class ResultUtil {

    public static<T> Result<T> checkResult(String result, Class<T> dataClass) {
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject.containsKey("errcode")) {
            int errCode = jsonObject.getInt("errcode");
            if (errCode != 0){
                return new Result<>(jsonObject.getStr("errcode"), jsonObject.getStr("errmsg"));
            }
        }
        if (dataClass != null) {
            return new Result(JSONUtil.toBean(result, dataClass));
        } else {
            return new Result<>(true);
        }
    }

}
