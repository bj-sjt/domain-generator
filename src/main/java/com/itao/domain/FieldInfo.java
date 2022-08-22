package com.itao.domain;

import lombok.Data;

/**
 * @Description 字段相关信息
 * @Author sjt
 * @CreateTime 2022-08-22 10:23
 */
@Data
public class FieldInfo {
    /**
     * 列名
     */
    private String column;
    /**
     * jdbc类型
     */
    private String jdbcType;
    /**
     * 字段名
     */
    private String field;
    /**
     * java类型
     */
    private String javaType;
    /**
     * 注释
     */
    private String comment;
}
