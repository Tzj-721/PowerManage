package com.powerManager.sys.service.impl;

import java.io.File;
import java.io.FileOutputStream;

public class PathTest {
    public static void main(String[] args) throws Exception {
        String path = "../ui/ws-ui/static/1.txt";
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        System.out.println("ok");
    }
}
