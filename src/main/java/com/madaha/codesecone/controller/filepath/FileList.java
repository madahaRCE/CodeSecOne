package com.madaha.codesecone.controller.filepath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * 任意路径遍历
 */
@RestController
@RequestMapping("/FileList")
public class FileList {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @poc http://127.0.0.1:28888/FileList/vul?filename=../../../
     *
     * @param filename
     * @return
     */
    @GetMapping("/vul")
    public String fileList(String filename){
        String filePath = System.getProperty("user.dir") + "/logs" + filename;
        log.info("[vul] 任意路径遍历：" + filePath);

        StringBuilder stringBuilder = new StringBuilder();

        File f = new File(filename);
        File[] fs = f.listFiles();

        if (fs != null){
            for (File ff : fs){
                stringBuilder.append(ff.getName()).append("<br>");
            }
            return stringBuilder.toString();
        }

        return filePath + "目录不存在";
    }
}
