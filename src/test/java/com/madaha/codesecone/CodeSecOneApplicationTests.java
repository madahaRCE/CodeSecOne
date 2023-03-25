package com.madaha.codesecone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

@SpringBootTest
class CodeSecOneApplicationTests {

    @Test
    void contextLoads() throws Exception {

        /**
         * 文件 --> 文件读取，字节输入流 --> 字符输入流 --> 字符缓冲流 --> 字符串
         */

        String filename = "payload_base64_encode.txt";
        final String path = "src/main/resources/static/evil/ysoserial/" + filename;
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);

        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();

        String base64Str = new BASE64Encoder().encode(bytes);

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(base64Str);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }
}
