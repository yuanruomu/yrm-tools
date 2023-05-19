package cn.yrm.tools.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="用户登录返回对象", description="用户登录返回对象")
public class UserLogin<T> {

    @ApiModelProperty(value = "登陆Token")
    private String token;

    @ApiModelProperty(value = "过期时间")
    private Long expireIn;

    @ApiModelProperty(value = "第三方OpenId")
    private String openId;

    @ApiModelProperty(value = "用户信息")
    private T user;
}
