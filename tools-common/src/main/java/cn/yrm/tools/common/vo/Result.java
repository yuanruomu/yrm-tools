package cn.yrm.tools.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuanr
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable, CodeImpl {

    private static final long serialVersionUID = -2042618546543630713L;

    public Result() {
    }

    /** 以下错误返回 */
    public Result(String code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public Result(CodeImpl codeImpl) {
        this.success = false;
        this.code = codeImpl.getCode();
        this.message = codeImpl.getMessage();
    }

    public static Result error(String message) {
        Result r = new Result();
        r.setMessage(message);
        r.setSuccess(false);
        return r;
    }

    /** 以下正确返回 */
    public Result(T data) {
        this.success = true;
        this.data = data;
    }

    public Result(T data, int count) {
        this.success = true;
        this.data = data;
        this.count = (long) count;
    }

    public Result(T data, long count) {
        this.success = true;
        this.data = data;
        this.count = count;
    }

    public Result(Boolean result) {
        this.success = true;
        this.data = (T) result;
    }

    public static Result success(String message) {
        Result r = new Result();
        r.setMessage(message);
        r.setSuccess(true);
        return r;
    }

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;
    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = "";
    /**
     * 错误号
     */
    @ApiModelProperty(value = "返回错误号")
    private String code = "0";
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据对象")
    private T data;
    /**
     * 返回数据条数
     */
    @ApiModelProperty(value = "返回数据条数")
    private Long count;
}
