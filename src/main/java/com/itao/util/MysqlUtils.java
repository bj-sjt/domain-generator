package com.itao.util;

import com.itao.domain.DbInfo;

import java.sql.*;

/**
 * @Description mysql工具类
 * @Author sjt
 * @CreateTime 2022/07/04 15:00:10
 */
public class MysqlUtils {

    private static final DbInfo dbInfo;
    static {
        dbInfo = DbInfo.getInstance();
        try {
            Class.forName(dbInfo.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUserName(), dbInfo.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet resultSet(String sql){
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
