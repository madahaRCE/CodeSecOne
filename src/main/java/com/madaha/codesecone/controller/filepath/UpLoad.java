package com.madaha.codesecone.controller.filepath;

import com.fasterxml.uuid.Generators;
import com.madaha.codesecone.util.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 任意文件上传：（直接上传jsp文件，获取webshell）
 * 1、通过 ClassLoader 类加载机制，注入恶意“字节码”；
 * 2、通过 Reflection 反射机制，造成任意代码执行；
 *
 * 利用“类加载”或“Agent”机制，注入内存马；
 */
@Controller
@RequestMapping("/UPLOAD")
public class UpLoad {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    // Save the uploaded file this folder!
    private static final String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/fileUpload/";
    private static String randomFilePath = "";

    // 请求 Controller 返回 html 视图。
    @RequestMapping("/upload")
    public String uploadStatus(){
        return "upload";
    }

    @PostMapping("/uploadVul")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件！uploadVul");
            return "redirect:upload";
        }

        try{
            byte[] bytes = file.getBytes();
            Path dir = Paths.get(UPLOAD_FOLDER);
            Path path = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());

            if (!Files.exists(dir)){
                Files.createDirectories(dir);
            }

            Files.write(path, bytes);
            log.info("[vul] 上传文件：" + path);

            // 该类继承 extends Model，返回前端 “org.springframework.ui”、“org.springframework.web.servlet.mvc.support” 视图数据。
            redirectAttributes.addFlashAttribute("message", "上传成功：" + path + "（不要将绝对路径暴露出来！）");
        } catch (Exception e){
            return e.toString();
        }

        return "redirect:upload";
    }

    @PostMapping("/uploadSafe")
    public String singleFileUploadSafe(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件！uploadSafe");
            return "redirect:upload";
        }

        try{
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_FOLDER + fileName);

            // 获取文件后缀名，并索引到最后一个，避免使用 .jpg.jsp 来绕过
            // 断言：用于判断上传文件的源文件名不为空，若不满足断言条件，即 报错！
            assert fileName != null;
            String Suffix = fileName.substring(fileName.lastIndexOf("."));

            String[] SuffixSafe = {".jpg", ".png", ".jpeg", ".gif", ".bmp", ".ico"};
            boolean flag = false;

            // 如果满足后缀名单，返回true
            for (String s : SuffixSafe){
                if (Suffix.toLowerCase().equals(s)){
                    flag = true;
                    break;
                }
            }

            log.info("[safe] 上传漏洞-白名单后缀模式：" + fileName);

            if(!flag){
                redirectAttributes.addFlashAttribute("message", "只允许上传图片，[.jpg, .png, .jpeg, .gif, .bmp, .ico]");
            }else {
                Files.write(path, bytes);
                redirectAttributes.addFlashAttribute("message", "上传文件成功：" + path + "（不要将绝对路径暴露出来！）");
            }
        } catch (Exception e){
            return e.toString();
        }

        return "redirect:upload";
    }

    /**
     * only upload picture!
     *
     * @param mutifile
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/uploadSafe++")
    public String uploadPicture(@RequestParam("file") MultipartFile mutifile, RedirectAttributes redirectAttributes) throws Exception{
        if (mutifile.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件！uploadSafe++");
            return "redirect:upload";
        }

        String fileName = mutifile.getOriginalFilename();
        String Suffix = fileName.substring(fileName.lastIndexOf("."));     // 获取文件后缀
        String mimeType = mutifile.getContentType();      // 获取MIME类型
        String filePath = UPLOAD_FOLDER + fileName;       // 绝对路径 + 文件名


        // 校验1：判断文件后缀名是否在白文件内
        String[] picSuffixList = {".jpg", ".png", ".jpeg", ".gif", ".bmp", ".ico"};
        boolean suffixFlag = false;
        for (String whilt_suffix : picSuffixList) {
            if (Suffix.toLowerCase().equals(whilt_suffix)){
                suffixFlag = true;
                break;
            }
        }
        if (!suffixFlag){
            log.error("[-] Suffix error: " + Suffix);
            redirectAttributes.addFlashAttribute("message", "只允许上传图片，[\".jpg\", \".png\", \".jpeg\", \".gif\", \".bmp\", \".ico\"]");
            return "redirect:upload";
        }

        // 校验2：判断 MIME 类型是否在黑名单内
        String[] mimeTypeBlackList = {
                "text/html",
                "text/javascript",
                "application/javascript",
                "application/ecmascript",
                "text/xml",
                "application/xml"
        };
        for(String blackMimeType : mimeTypeBlackList){
            // 用 contains 是为了防止 text/html;charset=UTF-8 绕过
            // replaceSpecialStr() 将非 <code>0-9a-1/-.<code/> 的字符替换为空
            // 首先获取 MIMETYPT 的value，然后将其字符转换为小写 并替换特殊字符，最后与blackMimeType黑名单进行对比。
            if (Security.replaceSpecialStr(mimeType).toLowerCase().contains(blackMimeType)){
                log.error("[-] Mime type error: " + mimeType);
                redirectAttributes.addFlashAttribute("message", "以下MIME类型禁止上传：[\"text/html\",\"text/javascript\",\"application/javascript\",\"application/ecmascript\",\"text/xml\",\"application/xml\"]  （不要将该描述返回前端！）");
                return "redirect:upload";
            }
        }


        // 作用：将上传文件进行临时保存，判断文件内容 是否为图片后，再确认是否保存在 服务器上。
        File excelFile = convert(mutifile);

        // 校验3：判断文件内容是否为 图片
        boolean isImageFlag = isImage(excelFile);
        deleteFile(randomFilePath);         // 通过 isImage() 临时将文件保存在服务器上，进行文件内容检测后再讲其删除。
        if (!isImageFlag){
            log.error("[-] File is not Image");
            redirectAttributes.addFlashAttribute("message", "该文件不是图片！读取的图片类型是有限制的，可以读取图片的格式为：[BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]  （不要将该描述返回前端！）");
            return "redirect:upload";
        }


        // 保存文件：通过以上校验后，即可将上传的文件保存到 服务器上。
        try{
            // Get the file and save it somewhere
            byte[] bytes = mutifile.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + mutifile.getOriginalFilename());
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute("message", "上传文件成功：" + path + "（不要将绝对路径暴露出来！）");
        }catch (IOException e){
            log.error(e.toString());
            redirectAttributes.addFlashAttribute("message", "服务端异常！");
            return "redirect:upload";
        }

        // 重定向 到文件上传页面
        return "redirect:upload";
    }

    // 删除上传文件 绝对路径上的 文件
    private void deleteFile(String filePath){
        File delFile = new File(filePath);
        if (delFile.isFile() && delFile.exists()){
            if(delFile.delete()){
                log.info("[+] " + filePath + " delete successfully!");;
                return;
            }
        }
        log.info(filePath + " delete failed!");
    }

    /**
     * 为上传文件，进行随机命名：
     *
     *      为了使用 ImageIO.read()
     *
     *      不建议使用 transferTO，因为原始的 MutipartFile 会被覆盖。
     *      https://stackoverflow.com/questions/24339990/how-to-convert-a-multipart-file-to-file
     *
     * @param multiFile
     * @return
     * @throws Exception
     *
     * convert：转换
     */
    private File convert(MultipartFile multiFile) throws Exception{
        String fileName = multiFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        UUID uuid = Generators.timeBasedGenerator().generate();
        randomFilePath = UPLOAD_FOLDER + uuid + suffix;
        // 随机生成一个同后缀的文件
        File convFile = new File(randomFilePath);

        boolean ret = convFile.createNewFile();
        if (!ret){
            return null;
        }

        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multiFile.getBytes());
        fos.close();

        return convFile;
    }

    /**
     * Check if the file is a picture.
     *
     * BufferedImage是其Image抽象类的实现类，是一个带缓冲区图像类。
     *
     * ImageIO.read()
     *    作用：将一幅图片加载到内存中（BufferedImage生成的图片在内存里有一个图像缓冲区，利用这个缓冲区我们可以很方便地操作这个图片），
     *         提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。
     *    效果：所以 可以用于确认，该文件是否为图片，确认文件内容为图片类型。
     *    限制：读取的图片类型是有限制的，可以读取图片的格式为：[BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean isImage(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        return bufferedImage != null;
    }
}
