package cn.yrm.tools.aliyun.sms;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yrm.tools.common.vo.Result;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliMessageClient {

    private IAcsClient acsClient;

    private boolean isMock;

    private String signName;

    AliMessageClient(String accessKeyId, String accessKeySecret, String signName, boolean isMock) {
        this.isMock = isMock;
        if (isMock) {
            return;
        }
        this.signName = signName;
        try {
            // 初始化ascClient需要的几个参数
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            // 短信API产品名称（短信产品名固定，无需修改）
            final String product = "Dysmsapi";
            // 短信API产品域名（接口地址固定，无需修改）
            final String domain = "dysmsapi.aliyuncs.com";
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("初始化阿里云短信客户端失败", e);
            throw new RuntimeException(e);
        }
    }

    public Result sendSms(String phone, String templateCode, String paramJson) {
        if (isMock) {
            return Result.success();
        }
        try {
            // 组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            // 使用post提交
            request.setMethod(MethodType.POST);
            // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
            request.setPhoneNumbers(phone);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName(this.signName);
            // 必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(paramJson);
            // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            // request.setSmsUpExtendCode("90997");
            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            // request.setOutId("yourOutId");
            // 请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return Result.success();
            } else {
                log.error(StrUtil.format("phone:{},template:{},param:{}", phone, templateCode, paramJson));
                log.error(JSONUtil.toJsonStr(sendSmsResponse));
                return Result.error(sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("短信发送失败", e);
            return Result.error(e.getMessage());
        }
    }
}
