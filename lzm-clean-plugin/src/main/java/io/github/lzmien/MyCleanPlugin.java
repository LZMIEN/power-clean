package io.github.lzmien;

import cn.hutool.core.io.FileUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author lizemin
 * @version 1.0
 * @since jdk 1.8
 * 插件名称： power-clean
 * 插件创建日期： 2025/3/15 9:09
 * 插件功能描述: 用于清理项目编译后的文件
 */
@Mojo(name = "power-clean", defaultPhase = LifecyclePhase.CLEAN) // 为插件任务起名字，方便调用
public class MyCleanPlugin extends AbstractMojo {

    /**
     * 接收files标签中的参数
     */
    @Parameter(property = "fileSettings")
    private List<fileSetting> fileSettings;

    public static class fileSetting {
        /**
         * 接收dir标签中的参数, 默认是相对路径
         */
        @Parameter(property = "dir")
        private String dir;

        /**
         * 接收include标签中的参数
         */
        @Parameter(property = "includes")
        private List<String> includes;

    }

    @Override
    public void execute() {
        getLog().info("开始清理项目中的产物包...");
        String proDir = System.getProperty("user.dir");
        getLog().info("项目目录为：" + proDir);
        File targetDir = new File(proDir, "/target");
        if (targetDir.exists()) {
            System.out.println("开始清理target目录...");
            FileUtil.del(targetDir);
            getLog().info("已清理target目录");
        } else {
            getLog().info("target目录不存在，无需清理...");
        }
        if (fileSettings == null || fileSettings.isEmpty()) {
            getLog().info("用户未指定需要额外清理的文件");
            return;
        }
        fileSettings.forEach(setting -> {
            File extraDir = new File(proDir, setting.dir);
            if (!extraDir.exists()) {
                getLog().info("用户指定的目录" + extraDir.getAbsolutePath() + "不存在，无需清理...");
                return;
            }
            getLog().info("开始清理" + extraDir.getAbsolutePath() + "目录...");
            if (Objects.isNull(setting.includes)) {
                FileUtil.del(extraDir);
                getLog().info("已删除目录" + extraDir.getAbsolutePath());
            } else {
                for (String pattern : setting.includes) {
                    PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
                    try (Stream<Path> stream = Files.walk(extraDir.toPath())) {
                        stream.forEach(path -> {
                            if (Files.isRegularFile(path)) {
                                if (pathMatcher.matches(path)) {
                                    FileUtil.del(path);
                                    getLog().info("已删除文件：" + path);
                                }
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
