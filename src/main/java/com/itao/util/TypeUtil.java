package com.itao.util;

/**
 * @Description jdbc类型转java类型
 * @Author sjt
 * @CreateTime 2022/07/04 16:11:34
 */
public class TypeUtil {

    public static String getJavaType(String sqlType, String field) {

        if (StringUtil.isBlank(sqlType) && StringUtil.isBlank(field)) {
            throw new RuntimeException("数据库类型或字段名不能为空");
        }

        if (sqlType.contains("tinyint")) {
            if (field.endsWith("flag")) {
                return "Boolean";
            }
            return "Integer";
        } else if (sqlType.contains("bigint")) {
            return "Long";
        } else if (sqlType.contains("int")) {
            return "Integer";
        } else if (sqlType.contains("double")) {
            return "Double";
        } else if (sqlType.contains("date")) {
            return "Date";
        } else if (sqlType.contains("decimal")) {
            return "BigDecimal";
        } else {
            return "String";
        }
    }

    public static String getJdbcType(String sqlType, String field) {

        if (StringUtil.isBlank(sqlType) && StringUtil.isBlank(field)) {
            throw new RuntimeException("数据库类型或字段名不能为空");
        }

        if (sqlType.contains("tinyint")) {
            return "TINYINT";
        } else if (sqlType.contains("bigint")) {
            return "BIGINT";
        } else if (sqlType.contains("int")) {
            return "INTEGER";
        } else if (sqlType.contains("date")) {
            if (field.endsWith("time")) {
                return "TIMESTAMP";
            }
            return "DATE";
        } else {
            return "VARCHAR";
        }
    }
}
