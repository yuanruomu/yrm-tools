package cn.yrm.tools.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yrm.tools.common.code.CommonConstant;
import cn.yrm.tools.common.code.QueryRuleEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueryGenerator {

    /**
     * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
     *
     * @param searchObj    查询实体
     * @param parameterMap request.getParameterMap() 扩展参数
     * @return QueryWrapper实例
     */
    public static <T> QueryWrapper<T> initQueryWrapper(T searchObj, Map<String, String[]> parameterMap) {
        long start = System.currentTimeMillis();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        convertWrapper(queryWrapper, searchObj, parameterMap);
        log.debug("---查询条件构造器初始化完成,耗时:" + (System.currentTimeMillis() - start) + "毫秒----");
        return queryWrapper;
    }

    /**
     * 组装 Mybatis Plus 查询条件
     */
    private static void convertWrapper(QueryWrapper<?> queryWrapper, Object searchObj, Map<String, String[]> parameterMap) {
        // 转换Order条件成Map
        HashMap<String, String> orderMap = convertOrderToMap(parameterMap);
        // 遍历查询对象的所有字段
        Field[] fields = ReflectUtil.getFields(searchObj.getClass());
        for (Field field : fields) {
            try {
                // 如果字段加注解了@TableField(exist = false),不走DB查询
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) {
                    continue;
                }
                String fieldName = field.getName();
                // 判断加入排序条件
                if(orderMap.containsKey(fieldName)){
                    String orderType = orderMap.get(fieldName);
                    if (CommonConstant.ORDER_ASC.equalsIgnoreCase(orderType)) {
                        queryWrapper.orderByAsc(StrUtil.toUnderlineCase(fieldName));
                    } else {
                        queryWrapper.orderByDesc(StrUtil.toUnderlineCase(fieldName));
                    }
                }
                String fieldType = field.getDeclaringClass().getName();
                // 添加 判断是否范围匹配
                if (parameterMap != null && parameterMap.containsKey(fieldName + CommonConstant.RANGE_BEGIN)) {
                    String beginValue = parameterMap.get(fieldName + CommonConstant.RANGE_BEGIN)[0].trim();
                    addQueryCondition(queryWrapper, fieldName, fieldType, beginValue, QueryRuleEnum.GE);
                }
                if (parameterMap != null && parameterMap.containsKey(fieldName + CommonConstant.RANGE_END)) {
                    String endValue = parameterMap.get(fieldName + CommonConstant.RANGE_END)[0].trim();
                    addQueryCondition(queryWrapper, fieldName, fieldType, endValue, QueryRuleEnum.LE);
                }
                // 多值查询
                if (parameterMap != null && parameterMap.containsKey(fieldName + CommonConstant.MULTI_SUFFIX)) {
                    String multiValue = parameterMap.get(fieldName + CommonConstant.MULTI_SUFFIX)[0].trim();
                    addQueryCondition(queryWrapper, fieldName.replace(CommonConstant.MULTI_SUFFIX, ""), fieldType, multiValue, QueryRuleEnum.IN);
                }
                // 获取字段值 以下判定查询条件
                Object value = ReflectUtil.getFieldValue(searchObj, field);
                if (value == null) {
                    continue;
                }
                // 判断单值  参数带不同标识字符串 走不同的查询
                QueryRuleEnum rule = getQueryRuleByValue(value);
                addQueryCondition(queryWrapper, fieldName, rule, replaceValue(rule, value));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static void addQueryCondition(QueryWrapper<?> queryWrapper, String fieldName, String fieldType, String value, QueryRuleEnum rule) {
        Object val = convertValue2Object(fieldType, value, rule);
        addQueryCondition(queryWrapper, fieldName, rule, val);
    }

    /**
     * 字段排序
     *
     * @param parameterMap
     */
    private static HashMap<String, String> convertOrderToMap(Map<String, String[]> parameterMap) {
        HashMap<String, String> orderMap = new HashMap<>();
        if (parameterMap == null || !parameterMap.containsKey(CommonConstant.PARAM_ORDER)) {
            return orderMap;
        }
        String orderStr = parameterMap.get(CommonConstant.PARAM_ORDER)[0];
        if (StrUtil.isEmpty(orderStr)) {
            return orderMap;
        }
        String[] orders = orderStr.split(CommonConstant.MULTI_SPLIT);
        for (String orderItem : orders) {
            String[] orderArr = orderItem.split(StrUtil.SPACE);
            if (orderArr.length == 2) {
                String column = orderArr[0];
                String orderType = orderArr[1];
                if (CommonConstant.ORDER_ASC.equalsIgnoreCase(orderType) || CommonConstant.ORDER_DESC.equalsIgnoreCase(orderType)) {
                    orderMap.put(column, CommonConstant.ORDER_ASC);
                }
            }
        }
        return orderMap;
    }

    /**
     * 根据所传的值 转化成对应的比较方式
     * 支持><= like in !
     *
     * @param value
     * @return
     */
    private static QueryRuleEnum getQueryRuleByValue(Object value) {
        QueryRuleEnum rule = null;
        if (value instanceof CharSequence) {
            String val = value.toString().trim();
            if (val.contains(CommonConstant.LIKE_STAR)) {
                if (val.startsWith(CommonConstant.LIKE_STAR) && val.endsWith(CommonConstant.LIKE_STAR)) {
                    rule = QueryRuleEnum.LIKE;
                } else if (val.startsWith(CommonConstant.LIKE_STAR)) {
                    rule = QueryRuleEnum.LEFT_LIKE;
                } else if (val.endsWith(CommonConstant.LIKE_STAR)) {
                    rule = QueryRuleEnum.RIGHT_LIKE;
                }
            } else if (val.contains(CommonConstant.MULTI_SPLIT)) {
                rule = QueryRuleEnum.IN;
            }
        }
        return rule != null ? rule : QueryRuleEnum.EQ;
    }

    /**
     * 替换掉关键字字符
     *
     * @param rule
     * @param value
     * @return
     */
    private static Object replaceValue(QueryRuleEnum rule, Object value) {
        if (!(value instanceof CharSequence)) {
            return value;
        }
        String val = value.toString().trim();
        if (rule == QueryRuleEnum.IN) {
            return val.split(",");
        }
        return val.replace(CommonConstant.LIKE_STAR, StrUtil.EMPTY);
    }

    private static Object convertValue2Object(String type, String value, QueryRuleEnum rule) {
        Object temp;
        switch (type) {
            case "class java.lang.Integer":
                temp = Integer.parseInt(value);
                break;
            case "class java.math.BigDecimal":
                temp = new BigDecimal(value);
                break;
            case "class java.lang.Short":
                temp = Short.parseShort(value);
                break;
            case "class java.lang.Long":
                temp = Long.parseLong(value);
                break;
            case "class java.lang.Float":
                temp = Float.parseFloat(value);
                break;
            case "class java.lang.Double":
                temp = Double.parseDouble(value);
                break;
            case "class java.util.Date":
                if (QueryRuleEnum.GE.equals(rule)) {
                    temp = DateUtil.beginOfDay(DateUtil.parse(value));
                } else if (QueryRuleEnum.LE.equals(rule)) {
                    temp = DateUtil.endOfDay(DateUtil.parse(value));
                } else {
                    temp = DateUtil.parse(value);
                }
                break;
            default:
                temp = value;
                break;
        }
        return temp;
    }

    /**
     * 根据规则走不同的查询
     *
     * @param queryWrapper QueryWrapper
     * @param name         字段名字
     * @param rule         查询规则
     * @param value        查询条件值
     */
    private static void addQueryCondition(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return;
        }
        name = StrUtil.toUnderlineCase(name);
        log.info("--查询规则-->" + name + " " + rule.getValue() + " " + value);
        switch (rule) {
            case GT:
                queryWrapper.gt(name, value);
                break;
            case GE:
                queryWrapper.ge(name, value);
                break;
            case LT:
                queryWrapper.lt(name, value);
                break;
            case LE:
                queryWrapper.le(name, value);
                break;
            case EQ:
                queryWrapper.eq(name, value);
                break;
            case NE:
                queryWrapper.ne(name, value);
                break;
            case IN:
                if (value instanceof String) {
                    queryWrapper.in(name, (Object[]) value.toString().split(","));
                } else if (value instanceof String[]) {
                    queryWrapper.in(name, (Object[]) value);
                } else if (value.getClass().isArray()) {
                    queryWrapper.in(name, (Object[]) value);
                } else {
                    queryWrapper.in(name, value);
                }
                break;
            case LIKE:
                queryWrapper.like(name, value);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(name, value);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(name, value);
                break;
            default:
                log.info("--查询规则未匹配到---");
                break;
        }
    }

}
