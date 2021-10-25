package com.demo.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiangchijie
 * @date 2021/10/25 10:42 上午
 */
public class TestFilesWalkFileTree {

    public static void main(String[] args) {
    }

    // 删除文件夹
    // visitFile 删除文件
    // postVisitDirectory 删除文件夹

    public static void m1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get(""), new SimpleFileVisitor<Path>() {
            // 进入文件夹之前
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>" + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            // 访问文件时
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>" + file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }

        });
        System.out.println("dir count" + dirCount.get());
        System.out.println("file count" + dirCount.get());
    }

    public static void m2(String[] args) throws IOException {
        Files.walkFileTree(Paths.get(""), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith("java")) {
                    System.out.println(file);
                }
                return super.visitFile(file, attrs);
            }
        });
    }
}
