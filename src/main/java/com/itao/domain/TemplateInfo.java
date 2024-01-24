package com.itao.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description 生成模板的相关信息
 * @Author sjt
 * @CreateTime 2022-08-22 10:21
 */
@Data
public class TemplateInfo {
    /**
     * 模板生成的时间
     */
    private String genTime;

    /**
     * 基础报名
     */
    private String packageName;

    /**
     * 类注释
     */
    private String classComment;

    /**
     * 类名
     */
    private String className;

    /**
     * 类名首字母小写
     */
    private String lowerClassName;

    /**
     * 字段集合
     */
    List<FieldInfo> fieldInfos;

    public TemplateInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.genTime = sdf.format(new Date());
    }
}
