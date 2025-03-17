package cn.lizemin;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * @author lzm
 * @date 2025/3/15 9:18
 * @description
 */
public class BaseTest {

    @Test
    public void test_get_project_dir() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test_delete_file() {
        String proDir = System.getProperty("user.dir");
        File file = new File(proDir, "src/main/java/com/lizemin/temp");
        FileUtil.del(file);
    }

    @Test
    public void test_file_path() {
//        String pattern = "**.xml";
//        String pattern = "**plugin.java";
//        String pattern = "**.java";
//        String pattern = "**";
        String pattern = "/src/**";
        getTargetFiles(pattern);
    }

    private static void getTargetFiles(String pattern) {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        try (Stream<Path> stream = Files.walk(Paths.get(System.getProperty("user.dir")))) {
            stream.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    if (pathMatcher.matches(path)) {
                        System.out.println(path.getFileName().toString());
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
