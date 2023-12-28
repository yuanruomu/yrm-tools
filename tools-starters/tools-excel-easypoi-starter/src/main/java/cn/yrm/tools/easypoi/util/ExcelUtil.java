package cn.yrm.tools.easypoi.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.yrm.tools.common.annotation.SimpleExcel;
import cn.yrm.tools.common.service.IDictManager;
import cn.yrm.tools.common.vo.DictModel;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {

    /**
     * 从导入excel文件流中获取数据
     *
     * @param inputStream
     * @param params
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> getListFromImportExcel(InputStream inputStream, ImportParams params, Class<T> clazz) throws Exception {
        List<Map<String, Object>> list = ExcelImportUtil.importExcel(inputStream, Map.class, params);
        List<T> entityList = new ArrayList<>();
        // 获取表头定义信息
        HashMap<String, String> headerMap = new HashMap<>();
        HashMap<String, List<DictModel>> dictMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SimpleExcel excel = field.getAnnotation(SimpleExcel.class);
            if (excel != null) {
                headerMap.put(excel.name(), field.getName());
                if (StrUtil.isNotEmpty(excel.dictCode())) {
                    // 查询字典
                    IDictManager dictManager = SpringUtil.getBean(IDictManager.class);
                    dictMap.put(excel.name(), dictManager.getDictItems(excel.dictCode()));
                }
            }
        }
        list.stream().forEach(map -> {
            T entity = getEntity(headerMap, dictMap, map, clazz);
            if (entity != null) {
                entityList.add(entity);
            }
        });
        return entityList;
    }

    private static <T> T getEntity(Map<String, String> headerMap, Map<String, List<DictModel>> dictMap, Map<String, Object> valueMap, Class<T> clazz) {
        try {
            T entity = clazz.newInstance();
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                String key = entry.getKey();
                if (!headerMap.containsKey(key)) {
                    continue;
                }
                String fieldName = headerMap.get(key);
                Object value = entry.getValue();
                if (value == null || StrUtil.isEmpty(value.toString())) {
                    continue;
                }
                if (dictMap.containsKey(key)) {
                    // 需要处理字典转换
                    List<DictModel> dictModels = dictMap.get(key);
                    dictModels.stream().filter(dict -> dict.getLabel().equals(value.toString())).findFirst().ifPresent(dict -> {
                        BeanUtil.setFieldValue(entity, fieldName, dict.getValue());
                    });
                } else {
                    BeanUtil.setFieldValue(entity, fieldName, value);
                }
            }
            return entity;
        } catch (Exception e) {
            return null;
        }
    }
}
