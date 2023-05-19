package cn.yrm.tools.common.code;

public interface CommonConstant {

    /** 通用设计主键名 */
    String TABLE_KEY_DEFINE = "id";
    /** 通用树设计根节点ID值 */
    String ROOT_PARENT_ID = "0";

    /** 范围查询规则 添加后缀 */
    String RANGE_BEGIN = "_begin";
    String RANGE_END = "_end";
    /** 非String类型多值查询 添加后缀 */
    String MULTI_SUFFIX = "_multi";
    /** 多值查询 分隔符号 */
    String MULTI_SPLIT = ",";
    /** 模糊查询通配符 */
    String LIKE_STAR = "*";
    /** 排序字段参数 */
    String PARAM_ORDER = "order";
    /** 排序方式 */
    String ORDER_ASC = "asc";
    String ORDER_DESC = "desc";
}
