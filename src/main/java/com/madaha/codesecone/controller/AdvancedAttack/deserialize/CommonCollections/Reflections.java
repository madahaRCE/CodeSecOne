package com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections;

import java.lang.reflect.Field;


public class Reflections {

//    public static void main(String[] args) throws Exception{
//        String versionStr = System.getProperty("java.version");
//        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
//        System.out.println(javaVersion);       // 例如：1.8.0_65
//    }
//
//    public static void setAccessible(AccessibleObject member){
//        String versionStr = System.getProperty("java.version");
//        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
//        if (javaVersion < 12){
//            Permit.set
//        } else {
//            member.setAccessible(true);
//        }
//    }

    public static Field getField(Class<?> clazz, String fieldName){
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
//            setAccessible(field);
            field.setAccessible(true);
        }catch (NoSuchFieldException ex){
            if (clazz.getSuperclass() != null){
                field = getField(clazz.getSuperclass(), fieldName);
            }
        }
        return field;
    }

    public static void setFieldValue(Object obj, String fileName, Object value) throws Exception{
        Field field = getField(obj.getClass(), fileName);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception{
        Field field = getField(obj.getClass(), fieldName);
        return field.get(obj);
    }
}
