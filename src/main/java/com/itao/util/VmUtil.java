package com.itao.util;

import com.itao.domain.TemplateInfo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description Velocity 模板工具类
 * @Author sjt
 * @CreateTime 2022/07/04 15:16:47
 */
public class VmUtil {
    private static final VelocityEngine ve;

    static {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    public static void generate(Map<String, String> vm, TemplateInfo info, boolean hasBigDecimal, boolean hasDate){
        if (vm == null || vm.isEmpty()){
            return;
        }

        for (Map.Entry<String, String> entry : vm.entrySet()) {
            // 获取模板文件
            Template t = ve.getTemplate(entry.getKey());
            // 设置变量
            Map<String, Object> context = new HashMap<>();
            context.put("info", info);
            context.put("hasBigDecimal", hasBigDecimal);
            context.put("hasDate", hasDate);
            VelocityContext ctx = new VelocityContext(context);
            // 输出
            StringWriter sw = new StringWriter();
            t.merge(ctx,sw);
            String file = entry.getValue();
            Path path = Paths.get(file);
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (FileWriter fw = new FileWriter(file)){
                fw.write(sw.toString());
                fw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
