package com.madaha.codesecone.controller.filepath;

import com.madaha.codesecone.util.Security;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 任意文件读取，将读取的文件进行 base64 编码输出：
 *
 * 1、使用如下payload进行攻击，后面那一部分要做一个url编码，不然执行不成功的：
 *      http://localhost:8080/path_traversal/vul?filepath=../../../../../etc/passwd
 *
 *      http://localhost:8080/path_traversal/vul?filepath=/..\..\..\..\..\..\..\..\..\..\/windows/win.ini
 *      @poc http://localhost:8080/path_traversal/vul?filepath=/..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c/windows/win.ini
 *
 *
 * 2、return result：
 *      OyBmb3IgMTYtYml0IGFwcCBzdXBwb3J0DQpbZm9udHNdDQpbZXh0ZW5zaW9uc10NClttY2kgZXh0ZW5zaW9uc10NCltmaWxlc10NCltNYWlsXQ0KTUFQST0xDQo=
 *
 * base解码：
 *      ; for 16-bit app support
 *      [fonts]
 *      [extensions]
 *      [mci extensions]
 *      [files]
 *      [Mail]
 *      MAPI=1
 */
@RestController
@RequestMapping("/FileInclude")
public class FileInclude {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * payload:
     *      http://127.0.0.1:28888/FileInclude/vul?filepath=/..\..\..\..\..\..\..\..\..\..\/windows/win.ini
     *      http://127.0.0.1:28888/FileInclude/vul?filepath=/..\..\..\..\..\..\..\..\..\..\/Windows/System32/drivers/etc/hosts
     *
     * @poc1 http://127.0.0.1:28888/FileInclude/vul?filepath=../../../../../../../../../windows/win.ini
     * @poc2 http://127.0.0.1:28888/FileInclude/vul?filepath=/..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c/windows/win.ini
     * @poc3 http://127.0.0.1:28888/FileInclude/vul?filepath=/..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c..%5c/Windows/System32/drivers/etc/hosts
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    @GetMapping("/vul")
    public String getFile(String filepath) throws IOException{
        return getFileBase64(filepath);
    }

    @GetMapping("/sec")
    public String getFileSec(String filepath) throws IOException{
        if(Security.pathFilter(filepath) == null){
            logger.info("Illegal file path: " + filepath);
            return "Bad boy. Illegal file path.";
        }
        return getFileBase64(filepath);
    }

    // 用于获取文件内容，并将其进行 Base64 编码，输出文件内容。
    public String getFileBase64(String filepath) throws IOException{

        logger.info("Working directory: " + System.getProperty("user.dir"));
        logger.info("File path: " + filepath);

        File f = new File(filepath);

        /**
         * isFile()：判断是否文件，也许可能是文件或者目录
         * exists()：判断是否存在，可能不存在
         * 两个不一样的概念.
         *
         * java中的isDirectory()是检查一个对象是否是文件夹。返回值是boolean类型的。如果是则返回true，否则返回false。
         * 调用方法为：对象.isDirectory() 无需指定参数。
         */
        if (f.exists() && !f.isDirectory()){
            byte[] data = Files.readAllBytes(Paths.get(filepath));
            return new String(Base64.encodeBase64(data));
        }else {
            return "File doesn't exits or is not a file.";
        }
    }

    // 这里的main函数，是一个单独测试用的，没啥特殊意义
    public static void main(String[] argv) throws IOException{
        String aa = new String(Files.readAllBytes(Paths.get("pom.xml")), StandardCharsets.UTF_8);
        System.out.println(aa);
    }
}
