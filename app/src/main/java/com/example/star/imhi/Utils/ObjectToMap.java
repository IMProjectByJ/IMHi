package com.example.star.imhi.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 11599 on 2018/1/14.
 */

public class ObjectToMap {
    public ObjectToMap(){}
    public  Map<String,String> objecttomap(Object obj){
        Map<String,String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = "";
//            try {
//                value = (String) field.get(obj);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
            value = "!";
            try {
                System.out.println("ceshi++++   "+field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(fieldName,value);
        }
        return map;
    }
}
