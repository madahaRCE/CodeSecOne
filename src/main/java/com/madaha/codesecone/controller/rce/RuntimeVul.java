package com.madaha.codesecone.controller.rce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.madaha.codesecone.entity.CMDJson;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

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
     * 百度amis + 阿里巴巴fastjson + springboot-springMVC：成功实现前后端分分离，通过json进行数据传输、amis前端展示、springboot后端处理！！！
     * @param param
     * @return
     */
    //@GetMapping("/vulnJson")
    @PostMapping("/vulnJson")
    public String vulnJson(@RequestBody String param){
        //System.out.println(param);      // {"cmd_json":"calc"}     // 通过 @JSONField(name = "cmd_json") 进行转换；

        CMDJson cmd_json_object = JSONObject.parseObject(param, CMDJson.class);
        //System.out.println(cmd_json_object);   // CMDJson(cmd=calc, last_return_value=null)    // 通过 @JSONField(name = "cmd_json") 进行转换；
        //System.out.println(cmd_json_object.getCmd());

    /**
     * 方式一：没有配置编码，会乱码！！！
     **/
//        StringBuilder stringBuilder = new StringBuilder();
//        String line;
//        try {
//            Process process = Runtime.getRuntime().exec(cmd_json_object.getCmd());
//            InputStream inputStream = process.getInputStream();
//            //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//                stringBuilder.append("\n");  //增加一个换行。
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //System.out.println("stringBuilder:" + stringBuilder);
//
//        String return_string = stringBuilder.toString();


    /**
     * 方式二：没有配置编码，会乱码！！！
     */
        String return_string = "default";
        try {
            // return_string = IOUtils.toString(Runtime.getRuntime().exec(cmd_json_object.getCmd()).getInputStream(), "UTF-8");
            return_string = IOUtils.toString(Runtime.getRuntime().exec(cmd_json_object.getCmd()).getInputStream(), "GBK");  //这样设置，可以正常解析中文字符！
        } catch (Exception e) {
            e.printStackTrace();
        }


        cmd_json_object.setReturnValue(return_string);
        //System.out.println("return_string" + return_string);
        //System.out.println(cmd_json_object);

        final String return_json = JSON.toJSONString(cmd_json_object);
        //System.out.println("return_json: " + return_json);

        // 最终，返回 CMDJson 的 json 对象！！     @即，返回Java的该 pojo 对象的 json 表示！！！
        return return_json;
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
