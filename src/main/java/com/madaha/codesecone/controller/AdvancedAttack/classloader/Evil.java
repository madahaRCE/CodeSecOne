package com.madaha.codesecone.controller.AdvancedAttack.classloader;

/**
 * Evil_Class
 */
public class Evil {
    public Evil(){
        System.out.println("Evil success!!!");

        try{
            Runtime.getRuntime().exec("cmd /c calc.exe");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
