package com.nio.c1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author xiangchijie
 * @date 2021/10/25 2:51 下午
 */
public class TestFileCopy {

    public static void main(String[] args) throws IOException {
        String source = "file/word.txt";
        String target = "file/word1.txt";
        Files.walk(Paths.get(source)).forEach(path -> {
            String targetName = path.toString().replace(source, target);
            if (Files.isDirectory(path)) {

            }
            // 是普通文件
            else if (Files.isRegularFile(path)) {

            }
        });
    }

}
