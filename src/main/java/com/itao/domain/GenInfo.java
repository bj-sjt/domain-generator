package com.itao.domain;

import com.itao.util.StringUtil;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description 生成模板相关内容信息
 * @Author sjt
 * @CreateTime 2022/07/04 15:42:13
 */

@Getter
public class GenInfo {
    private static final GenInfo instance = new GenInfo();
    private final String packageName;
    private final String tables;
    private final String prefix;
    private final Path baseApiPath;
    private final Path baseServicePath;


    private GenInfo(){
        try {
            Properties properties = new Properties();
            InputStream inputStream = DbInfo.class.getClassLoader().getResourceAsStream("gen.properties");
            properties.load(inputStream);
            this.packageName = properties.getProperty("packageName");
            this.tables = properties.getProperty("tables");
            this.prefix = properties.getProperty("prefix");
            String filePath = properties.getProperty("filePath");
            String packagePath = packageName.replace(".", "/");
            boolean hasFilePath = true;
            if (StringUtil.isBlank(filePath)) {
                filePath = System.getProperty("user.dir");
                hasFilePath = false;
            }
            if (!hasFilePath) {
                String baseApi = filePath + "/src/main/java";
                String baseService = filePath + "/src/main/java";
                baseApiPath = Paths.get(baseApi, packagePath);
                baseServicePath = Paths.get(baseService, packagePath);
            } else {
                filePath = filePath.replace("\\", "/");
                int lastIndexOf = filePath.lastIndexOf("/");
                String substring = filePath.substring(lastIndexOf);
                String baseApi = filePath + "/" + substring + "-api/src/main/java";
                String baseService = filePath + "/" + substring + "-service/src/main/java";
                baseApiPath = Paths.get(baseApi, packagePath);
                baseServicePath = Paths.get(baseService, packagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GenInfo instance(){
        return instance;
    }

    public  Map<String, String> getVm(String className){
        return new HashMap<String, String>(){{
            put("vm/domain.vm", baseServicePath + "/service/domain/entity/" + className + ".java");
            put("vm/request.vm", baseApiPath + "/api/request/" + className + "Dto.java");
            put("vm/response.vm", baseApiPath + "/api/response/" + className + "Vo.java");
            put("vm/baseMapper.vm", baseServicePath + "/service/domain/mapper/" + className + "Mapper.java");
            put("vm/repository.vm", baseServicePath + "/service/domain/repository/" + className + "Repository.java");
            put("vm/repositoryImpl.vm", baseServicePath + "/service/domain/repository/impl/" + className + "RepositoryImpl.java");
            put("vm/service.vm", baseServicePath + "/service/domain/service/" + className + "Service.java");
            put("vm/serviceImpl.vm", baseServicePath + "/service/domain/service/impl/" + className + "ServiceImpl.java");
            put("vm/controller.vm", baseServicePath + "/service/interfaces/web/" + className + "Controller.java");
            //put("vm/mapper.vm", basePath + "/mapper/"  + className + "Mapper.xml");
        }};
    }
}
