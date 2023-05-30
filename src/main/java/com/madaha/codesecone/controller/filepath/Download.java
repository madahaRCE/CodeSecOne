package com.madaha.codesecone.controller.filepath;

import com.madaha.codesecone.util.SecurityUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/Download")
public class Download {

    Logger log = LoggerFactory.getLogger(Download.class);

    /**
     * @poc1 http://127.0.0.1:28888/Download/vul?filename=../../../../../../../Windows/System32/calc.exe
     *
     * @poc2 http://127.0.0.1:28888/Download/vul?filename=/..\..\..\..\..\..\/Windows/System32/drivers/etc/hosts
     * @poc_编码 http://127.0.0.1:28888/Download/vul?filename=/..%5C..%5C..%5C..%5C..%5C..%5C/Windows/System32/drivers/etc/hosts
     *
     * @param filename
     * @param response
     * @return
     */
    @GetMapping("/vul")
    public String download(String filename, HttpServletResponse response){
        // 下载的文件路径
        String filepath = System.getProperty("user.dir") + "/logs" + filename;
        log.info("[vul] 任意文件下载：" + filepath);

        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(Paths.get(filename)))) {
            // 如果不使用如下方法，那返回的文件名中就会存在类似“../../../calc.exe”的文件名，这里做了一下简单优化。
            // 如果是在正常的开发过程中，此处可能需要加吧，因为也不会有正常程序转入 目录穿越的文件名 吧。
            // trim() 函数移除字符串两侧的空白字符或其他预定义字符。
            // substring() 方法从字符串中提取两个索引（位置）之间的字符，并返回子字符串。 substring() 方法从头到尾（不包括）提取字符。
            String filenameTrim = filename.trim();
            String filenameSub = filenameTrim.substring(filenameTrim.lastIndexOf("/")+1);

            response.setHeader("Content-Disposition", "attachment; filename=" + filenameSub);
            // response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentLength((int)Files.size(Paths.get(filepath)));
            response.setContentType("application/octet-stream");

            // 使用 Apache Commons IO 库的工具方法将输入流中的数据拷贝到输出流中
            IOUtils.copy(inputStream, response.getOutputStream());
            log.info("文件 {} 下载成功，路径：{}", filename, filepath);

            return "下载文件成功：" + filepath;
        }catch (IOException e){
            log.error("下载文件失败，路径：{}",filepath, e);

            return "未找到文件：" + filepath;
        }
    }

    /**
     * @poc http://127.0.0.1:28888/Download/safe?filename=../../../../../../../Windows/System32/calc.exe
     *
     * @param filename
     * @return
     */
    @GetMapping("/safe")
    public String safe(String filename){
        if(!SecurityUtils.checkTraversal(filename)){
            String filePath = System.getProperty("user.dir") + "/log" +filename;
            return "安全路径：" + filePath;
        }else{
            return "监测到非法遍历！";
        }
    }








}
