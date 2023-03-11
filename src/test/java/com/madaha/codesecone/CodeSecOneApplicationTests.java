package com.madaha.codesecone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeSecOneApplicationTests {

    @Test
    void contextLoads() {
//        s1().s2();
    }

    void s1(){
        System.out.println("f1");
    }

    void s2(){
        System.out.println("f1");
    }

    void s3(){
        System.out.println("f1");
    }

    public static void f2(){
        System.out.println("f2");
    }
    public static String f3(){
        System.out.println("f3");
        return "f3";
    }
}
