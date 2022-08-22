package com.itao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串相关
 * @Author sjt
 * @CreateTime 2022/07/04 16:02:53
 */
public class StringUtil {

    private static final Pattern UNDER_LINT_PATTERN = Pattern.compile("_([a-z])");

    /**
     * 下划线转驼峰
     * @param source 需要转换的String
     */
    public static String under2hump(String source) {
        if (source == null) {
            return "";
        }
        Matcher matcher = UNDER_LINT_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 判断一个字符串是否是 null 或 ""
     * @param str 要判断的字符串
     */
    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 将字符串首字母大写
     * @param str 要转换的字符串
     */
    public static String capital(String str) {
        if (isBlank(str)) {
            return "";
        }
        String first = str.substring(0, 1);
        String after = str.substring(1);
        first = first.toUpperCase();
        return first + after;
    }
    /**
     * 将字符串首字母小写
     * @param str 要转换的字符串
     */
    public static String deCapital(String str) {
        if (isBlank(str)) {
            return "";
        }
        String first = str.substring(0, 1);
        String after = str.substring(1);
        first = first.toLowerCase();
        return first + after;
    }


}
