package com.madaha.codesecone.controller.AdvancedAttack.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 反射机制：不安全反射问题，造成任意代码执行
 */
@RestController
@RequestMapping("/Reflection")
public class Reflection {

    Logger log = LoggerFactory.getLogger(Reflection.class);

    /**
     * 反射构造 Runtime 类执行
     * @poc http://127.0.0.1:28888/Reflection/runtimeVul?cmd=calc
     */
    @GetMapping("/runtimeVul")
    public String runtimeVul(String cmd) {

        try {
            Class clazz = Class.forName("java.lang.Runtime");
            Method method_exec = clazz.getMethod("exec", String.class);
            Method method_runtime = clazz.getMethod("getRuntime");

            Object object = method_runtime.invoke(clazz);

            // method_exec.invoke(object, "calc");
            method_exec.invoke(object, cmd);

            return "命令执行成功";
        } catch (Exception e){
            e.printStackTrace();
            return "执行异常！！！";
        }
    }

    /**
     * 将上面的payload进行整合：
     *
     * @payload Class clazz = Class.forName("java.lang.Runtime");clazz.getMethod("exec", String.class).invoke(clazz.getMethod("getRuntime").invoke(clazz), cmd);
     *
     * @poc http://127.0.0.1:28888/Reflection/runtimeVul2?cmd=calc
     *
     * @param cmd 要执行的命令；
     * @return
     */
    @GetMapping("/runtimeVul2")
    public String runtimeVul2(String cmd) {

        try {
            Class clazz = Class.forName("java.lang.Runtime");

            clazz.getMethod("exec", String.class).invoke(clazz.getMethod("getRuntime").invoke(clazz), cmd);

            return "命令执行成功";
        } catch (Exception e){
            e.printStackTrace();
            return "执行异常！！！";
        }
    }


    /**
     * 设置setAccessible(true)，进行暴力访问权限设置；
     *
     * @poc http://127.0.0.1:28888/Reflection/setAccessibleVul?cmd=calc
     *
     * @param cmd
     * @return
     */
    @GetMapping("/setAccessibleVul")
    public String setAccessibleVul(String cmd){
        try{
            Class clazz = Class.forName("java.lang.Runtime");
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            clazz.getMethod("exec", String.class).invoke(constructor.newInstance(), cmd);

            return "命令执行成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "执行异常！";
        }
    }

    /**
     * 通过反序列化漏洞进一步利用，再通过 反射机制 实现任意命令执行。
     *
     * @poc http://127.0.0.1:28888/Reflection/DeserializeDemo
     *
     * @param cmd
     * @return
     */
    @GetMapping("/DeserializeDemo")
    public String DeserializeDemo(String cmd){
        try{

            Demo x = new Demo("XiaoBai",18);
            Operation operation = new Operation();

            operation.ser(x);
            operation.deser();

            return "命令执行成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "执行异常！";
        }
    }












}
