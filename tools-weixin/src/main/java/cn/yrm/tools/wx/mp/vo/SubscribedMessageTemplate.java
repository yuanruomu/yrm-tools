package cn.yrm.tools.wx.mp.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.yrm.tools.common.exception.BusinessException;
import cn.yrm.tools.wx.mp.code.ProgramStateEnum;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class SubscribedMessageTemplate {

    /**
     * 模板编号
     */
    private String templateId;
    /**
     * 模板参数名称列表
     */
    private List<String> paramKeyList;
    /**
     * 日期格式
     */
    private String dateFormat = "yyyy-MM-dd HH:mm";

    public SubscribedMessageTemplate() {
    }

    /**
     * 初始化订阅消息模板
     * @param templateId 模板ID
     * @param paramKeyList   参数名列表
     */
    public SubscribedMessageTemplate(String templateId, List<String> paramKeyList) {
        this.templateId = templateId;
        this.paramKeyList = paramKeyList;
    }

    /**
     * 初始化订阅消息模板
     * @param templateId 模板ID
     * @param paramKeyStr 逗号分隔参数名称
     */
    public SubscribedMessageTemplate(String templateId, String paramKeyStr) {
        this.templateId = templateId;
        this.paramKeyList = Arrays.asList(paramKeyStr.split(","));
    }

    /**
     * 构建订阅消息实体
     * @param toUser 接收消息用户OpenId
     * @param programStateEnum 小程序版本
     * @param params 参数
     * @return
     */
    public SubscribedMessage build(String toUser, ProgramStateEnum programStateEnum, Object ... params) {
        return build(toUser, null, programStateEnum, params);
    }

    /**
     * 构建订阅消息实体
     * @param toUser 接收消息用户OpenId
     * @param programStateEnum 小程序版本
     * @param toPage 小程序跳转路径
     * @param params 参数
     * @return
     */
    public SubscribedMessage build(String toUser, String toPage, ProgramStateEnum programStateEnum, Object ... params) {
        if (paramKeyList == null || paramKeyList.size() == 0) {
            throw new BusinessException("请先设置参数列表");
        }
        if (params.length != paramKeyList.size()) {
            throw new BusinessException("参数个数和模板不一致");
        }
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < paramKeyList.size(); i++) {
            if (paramKeyList.get(i).startsWith("date") || paramKeyList.get(i).startsWith("time") ) {
                jsonObject.set(paramKeyList.get(i), new JSONObject().set("value",
                        DateUtil.format((Date) params[i], this.dateFormat)));
            } else if (paramKeyList.get(i).startsWith("thing")) {
                jsonObject.set(paramKeyList.get(i), new JSONObject().set("value", StrUtil.sub(params[i].toString(), 0, 20)));
            } else if (paramKeyList.get(i).startsWith("phrase")) {
                jsonObject.set(paramKeyList.get(i), new JSONObject().set("value", StrUtil.sub(params[i].toString(), 0, 5)));
            } else if (paramKeyList.get(i).startsWith("name")) {
                jsonObject.set(paramKeyList.get(i), new JSONObject().set("value", StrUtil.sub(params[i].toString() ,0, 10)));
            } else {
                jsonObject.set(paramKeyList.get(i), new JSONObject().set("value", params[i].toString()));
            }
        }
        SubscribedMessage message = new SubscribedMessage()
                .setTemplate_id(this.templateId)
                .setTouser(toUser)
                .setData(jsonObject)
                .setMiniprogram_state(programStateEnum.getValue());
        if(StrUtil.isNotEmpty(toPage)) {
            message.setPage(toPage);
        }
        return message;
    }
}
