package cn.yrm.tools.common.util;


import cn.yrm.tools.common.vo.CodeImpl;
import cn.yrm.tools.common.exception.BusinessException;

/**
 * @author yuanr
 */
public final class ObjectUtil {

    public static <T> T requireNonNull(T obj, CodeImpl codeImpl) {
        if (obj == null) {
            throw new BusinessException(codeImpl);
        }
        return obj;
    }
}
