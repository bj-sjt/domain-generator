package com.itao.domain;


import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description 数据库相关信息
 * @Author sjt
 * @CreateTime 2022/07/04 14:48:56
 */
@Getter
public class DbInfo {

    private static final DbInfo instance = new DbInfo();
    private final String url;
    private final String userName;
    private final String password;
    private final String driverClass;

    private DbInfo(){
        try {
            Properties properties = new Properties();
            InputStream inputStream = DbInfo.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);
            this.url = properties.getProperty("url");
            this.userName = properties.getProperty("userName");
            this.password = properties.getProperty("password");
            this.driverClass = properties.getProperty("driverClass");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static DbInfo instance() {
        return instance;
    }

}
