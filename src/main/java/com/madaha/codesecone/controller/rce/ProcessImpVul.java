package com.madaha.codesecone.controller.rce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ProcessImpl 命令执行：  ProcessImpl 是更为底层的实现，Runtime 和 ProcessBuilder 执行命令，实际上也是调用了 ProcessImpl 这个类。
 */
@RestController
@RequestMapping("/RCE/ProcessImpl")
public class ProcessImpVul {

    @GetMapping("/vul")
    public static String vul(String cmd) throws Exception{
        // ProcessImpl 类是一个抽象类，不能够直接被调用，但是可以通过反射来间接调用 ProcessImpl 来达到命令执行的目的。
        // 首先，使用 Class.forName 方法来获取 ProcessImpl 类的类对象。
        Class clazz = Class.forName("java.lang.ProcessImpl");

        // 然后，使用 clazz.getDeclaredMethod 方法来获取 ProcessImpl 来达到执行明命令的目的。
        Method method = clazz.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);

        // 使用 method.setAccessible 方法设置 start 方法为可访问
        method.setAccessible(true);

        // 最后，使用 method.invoke 方法来调用 start 方法，并传入参数 cmd，执行命令
        Process process = (Process) method.invoke(null, new String[]{cmd}, null, null, null, false);

        // 获取命令输出
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder output = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null){
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
