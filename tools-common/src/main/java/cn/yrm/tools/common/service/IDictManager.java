package cn.yrm.tools.common.service;

import cn.yrm.tools.common.vo.DictModel;

import java.util.List;
import java.util.Map;

public interface IDictManager {
    /**
     * 移除某一个字典缓存
     * @param dictCode
     */
    void removeDictCache(String dictCode);
    /**
     * 保存某一个字典至缓存
     * @param dictCode
     * @param itemList
     */
    void saveDictCache(String dictCode, List<DictModel> itemList);

    /**
     * 获得某个字典
     * @param dictCode
     * @return
     */
    List<DictModel> getDictItems(String dictCode);

    /**
     * 从缓存获得所有字典
     * @return
     */
    Map<String, List<DictModel>> getAllFromCache();

    /**
     * 清空所有字典缓存
     */
    void clearAllFromCache();

    /**
     * Excel字典转换数组
     */
    List<String> getDictConvertString(String dictCode);
}
