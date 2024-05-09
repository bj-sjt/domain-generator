package com.itao;


import com.itao.domain.FieldInfo;
import com.itao.domain.GenInfo;
import com.itao.domain.TemplateInfo;
import com.itao.util.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Launcher {
    private static final String TABLE_COMMENT_SQL =
            "select table_comment from information_schema.tables where table_name = ";
    private static final String FIELD_SQL = "show full fields from ";

    private static final GenInfo genInfo;

    static {
        genInfo = GenInfo.instance();
    }

    public static void main(String[] args) {
        String[] tables = getTables();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (String t : tables) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                TemplateInfo info = templateInfo(t.trim());
                boolean hasBigDecimal = info.getFieldInfos().stream()
                        .anyMatch(fieldInfo -> "BigDecimal".equals(fieldInfo.getJavaType()));

                boolean hasDate = info.getFieldInfos().stream()
                        .anyMatch(fieldInfo -> "Date".equals(fieldInfo.getJavaType()));
                VmUtil.generate(genInfo.getVm(info.getClassName()), info, hasBigDecimal, hasDate);
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private static TemplateInfo templateInfo(String t) {
        try {
            TemplateInfo info = new TemplateInfo();
            info.setPackageName(genInfo.getPackageName());
            ResultSet resultSet =
                    MysqlUtils.resultSet(TABLE_COMMENT_SQL + "'" + t + "'");
            info.setClassComment(resultSet.next() ? resultSet.getString(1) : "");
            String className;
            if (StringUtil.isBlank(genInfo.getPrefix())) {
                className = StringUtil.capital(StringUtil.under2hump(t));
            } else {
                className = StringUtil.capital(StringUtil.under2hump(t.replace(genInfo.getPrefix(), "")));
            }
            info.setClassName(className);
            info.setLowerClassName(StringUtil.deCapital(className));
            info.setBasePath(StringUtil.camelToUnderline(info.getLowerClassName()).replace("_", "/"));
            List<FieldInfo> fieldInfos = new ArrayList<>();
            info.setFieldInfos(fieldInfos);
            ResultSet rs = MysqlUtils.resultSet(FIELD_SQL + t);
            while (rs.next()) {
                FieldInfo fieldInfo = getFieldInfo(rs);
                fieldInfos.add(fieldInfo);
            }
            System.out.println(info);
            return info;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static FieldInfo getFieldInfo(ResultSet rs) {
        try {
            FieldInfo fieldInfo = new FieldInfo();
            String field = rs.getString(1);
            String type = rs.getString(2);
            String comment = rs.getString(9);
            fieldInfo.setColumn(field);
            fieldInfo.setField(StringUtil.under2hump(field));
            fieldInfo.setComment(comment);
            fieldInfo.setJavaType(TypeUtil.getJavaType(type, field));
            fieldInfo.setJdbcType(TypeUtil.getJdbcType(type, field));
            return fieldInfo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getTables() {
        try {
            String table = genInfo.getTables();
            String[] tables;
            if (StringUtil.isBlank(table)) {
                throw new RuntimeException("没有指定表名");
            }
            if ("*".equals(table)) {
                List<String> names = new ArrayList<>();
                ResultSet rs = MysqlUtils.resultSet("show tables");
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    names.add(tableName);
                }
                tables = names.toArray(new String[0]);
            } else if (table.contains("%")) {
                List<String> names = new ArrayList<>();
                ResultSet rs = MysqlUtils.resultSet("show tables like '" + table + "'");
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    names.add(tableName);
                }
                tables = names.toArray(new String[0]);
            } else {
                tables = table.split(",");
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
