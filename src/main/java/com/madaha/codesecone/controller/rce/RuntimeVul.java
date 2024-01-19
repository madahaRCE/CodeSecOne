package com.madaha.codesecone.controller.rce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

//@Api(tags = "命令执行接口测试")
//@Tag(name = "RuntimeVul-controller", description = "命令执行-用户接口")
@RestController
@RequestMapping("/RCE/Runtime")
public class RuntimeVul {

    /**
     * @poc http://127.0.0.1:28888/RCE/Runtime/vuln?cmd=calc
     * */
    //@Operation(tags = "getRuntime.exec()命令执行", description = "参数cmd后，直接加要执行的命令")  //swagger3
    @GetMapping("/vuln")
    public static String vuln(String cmd){
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            /**
             * InputStream ： 是所有字节输入流的超类，一般使用它的子类：FileInputStream等，它能输出字节流；
             * InputStreamReader ： 是字节流与字符流之间的桥梁，能将字节流输出为字符流，并且能为字节流指定字符集，可输出一个个的字符；
             * BufferedReader ： 提供通用的缓冲方式文本读取，readLine读取一个文本行， 从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取。
             */

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

    /**
     * @poc http://127.0.0.1:28888/RCE/Runtime/safe?cmd=notepad
     * @param cmd
     * @return
     */
    @RequestMapping("/safe")
    public static String safe(String cmd){
        StringBuilder sb = new StringBuilder();
        String line;

        // 定义命令白名单
        Set<String> commands = new HashSet<String>();
        commands.add("calc");
        commands.add("whoami");

        // 检查用户提供的命令是否在白名单中
        String command = cmd.split("\\s+")[0];
        if (!commands.contains(command)){
            return "不在白名单中";
        }

        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            InputStream inputStream = proc.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
