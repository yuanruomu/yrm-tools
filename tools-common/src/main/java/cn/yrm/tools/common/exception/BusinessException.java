package cn.yrm.tools.common.exception;


import cn.yrm.tools.common.vo.CodeImpl;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private String code;

    @Getter
    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(CodeImpl codeImpl) {
        super(codeImpl.getMessage());
        this.code = codeImpl.getCode();
        this.message = codeImpl.getMessage();
    }
}
