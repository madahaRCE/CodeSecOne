package com.madaha.codesecone.controller.filepath;

import com.madaha.codesecone.util.Security;
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
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
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
        if(!Security.checkTraversal(filename)){
            String filePath = System.getProperty("user.dir") + "/log" +filename;
            return "安全路径：" + filePath;
        }else{
            return "监测到非法遍历！";
        }
    }








}
