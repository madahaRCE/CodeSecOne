package com.madaha.codesecone.controller.rce;

import com.madaha.codesecone.util.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/RCE/ProcessBuilder")
public class ProcessBuilderVul {

    /**
     * 需要先进行url编码（http://127.0.0.1:28888/RCE/ProcessBuilder/vul?filepath= c:\tmp&&calc）
     * @poc http://127.0.0.1:28888/RCE/ProcessBuilder/vul?filepath=%20c%3A%5Ctmp%26%26calc
     * @param filepath
     * @return
     * @throws IOException
     */
    @RequestMapping("/vul")
    public static String processBuilderVul(String filepath) throws IOException {

        // String[] cmdline = {"sh", "-c", "ls -l" + filepath};
        String[] cmdline = {"cmd", "/c", "dir" + filepath};

        ProcessBuilder processBuilder = new ProcessBuilder(cmdline);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
//            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    /**
     * @poc http://127.0.0.1:28888/RCE/ProcessBuilder/safe?filepath=%20c%3A%5Ctmp%26%26calc
     * @param filepath
     * @return
     * @throws IOException
     */
    @RequestMapping("/safe")
    public static String processBuliderSafe(String filepath) throws IOException{

        if (!SecurityUtils.chrckOs(filepath)){

            // String[] cmdList = {"sh", "-c", "ls -l" + filepath};
            String[] cmdList = {"cmd", "/c", "dir" + filepath};

            ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            StringBuilder output = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null){
                output.append(line).append("\n");
            }

            return output.toString();

        }else {
            return "监测到非法命令注入";
        }
    }
}
