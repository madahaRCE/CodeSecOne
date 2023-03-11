package com.madaha.codesecone.controller.rce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/RCE/Runtime")
public class RuntimeVul {

    /**
     * @poc http://127.0.0.1:8080/RCE/Runtime/val?cmd=id
     * */
    @GetMapping("/val")
    public static String vul(String cmd){
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            // 读取命令的输出
            InputStream inputStream = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();


        //因为使用 @RestController 注解，所以可以直接返回json数据。
        //return cmd;
    }
}
