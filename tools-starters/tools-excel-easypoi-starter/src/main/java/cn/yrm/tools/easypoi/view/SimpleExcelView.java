package cn.yrm.tools.easypoi.view;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.util.PoiReflectorUtil;
import cn.afterturn.easypoi.view.MiniAbstractExcelView;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.yrm.tools.common.annotation.Cell;
import cn.yrm.tools.common.annotation.SimpleExcel;
import cn.yrm.tools.common.service.IDictManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleExcelView extends MiniAbstractExcelView {

    public SimpleExcelView() {
        super();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 转换Excel输出格式
        Class<?> clz = (Class<?>) model.get(NormalExcelConstants.CLASS);
        List<ExcelExportEntity> exportEntityList = convertSimpleExcelToList(clz);
        // 导出Workbook
        Workbook workbook = ExcelExportUtil.exportExcel((ExportParams) model.get(NormalExcelConstants.PARAMS), exportEntityList,
                (Collection<?>) model.get(NormalExcelConstants.DATA_LIST));
        // 文件名
        String codedFileName = "临时文件";
        if (model.containsKey(NormalExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(NormalExcelConstants.FILE_NAME);
        }
        // 后缀名.xls或.xlsx
        if (workbook instanceof HSSFWorkbook) {
            codedFileName += HSSF;
        } else {
            codedFileName += XSSF;
        }
        // 编码
        if (isIE(request)) {
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    private List<ExcelExportEntity> convertSimpleExcelToList(Class<?> clz) {
        List<ExcelExportEntity> exportEntityList = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(clz);
        for (Field field : fields) {
            SimpleExcel simpleExcel = AnnotationUtil.getAnnotation(field, SimpleExcel.class);
            if (simpleExcel != null) {
                ExcelExportEntity excelExportEntity = new ExcelExportEntity(simpleExcel.name(), field.getName());
                if (StrUtil.isNotEmpty(simpleExcel.dictCode())) {
                    try {
                        IDictManager dictManager = SpringUtil.getBean(IDictManager.class);
                        List<String> dictStrList = dictManager.getDictConvertString(simpleExcel.dictCode());
                        String[] dictReplace = new String[dictStrList.size()];
                        excelExportEntity.setReplace(dictStrList.toArray(dictReplace));
                    } catch (Exception e) {
                        log.error("Dict convert error", e);
                    }
                }
                if (StrUtil.isNotEmpty(simpleExcel.format())) {
                    if (field.getType().getSimpleName().equals("Date")) {
                        excelExportEntity.setFormat(simpleExcel.format());
                    } else {
                        excelExportEntity.setNumFormat(simpleExcel.format());
                    }
                }
                excelExportEntity.setType(simpleExcel.type().getValue());
                if(Cell.IMAGE.equals(simpleExcel.type())){
                    excelExportEntity.setExportImageType(1);
                }
                excelExportEntity.setMethod(PoiReflectorUtil.fromCache(clz).getGetMethod(field.getName()));
                excelExportEntity.setWidth(simpleExcel.width());
                excelExportEntity.setHeight(simpleExcel.height());
                excelExportEntity.setOrderNum(simpleExcel.orderNum());
                exportEntityList.add(excelExportEntity);
            }
        }
        return exportEntityList;
    }
}
