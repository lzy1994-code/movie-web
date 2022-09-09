package com.bw.movie.util;

import com.bw.movie.rpc.pojo.Movies;
import com.bw.movie.rpc.vo.MovieVo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xyj on 2018/7/17.
 */
public class CopyBean {

    public static <T> T getBean(T TargetBean, T SourceBean) throws Exception {
        if (TargetBean == null) {
            return null;
        }
        Field[] tFields = TargetBean.getClass().getDeclaredFields();
        Field[] sFields = SourceBean.getClass().getDeclaredFields();
        try {
            for (Field field : tFields) {
                String fieldName = field.getName();
                if (fieldName.equals("serialVersionUID"))
                    continue;
                if (field.getType() == Map.class)
                    continue;
                if (field.getType() == Set.class)
                    continue;
                if (field.getType() == List.class)
                    continue;
                for (Field sField : sFields) {
                    if (!sField.getName().equals(fieldName)) {
                        continue;
                    }
                    Class type = field.getType();
                    String setName = getSetMethodName(fieldName);
                    Method tMethod = TargetBean.getClass().getMethod(setName, new Class[]{type});
                    String getName = getGetMethodName(fieldName);
                    Method sMethod = SourceBean.getClass().getMethod(getName, null);
                    Object setterValue = sMethod.invoke(SourceBean, null);
                    tMethod.invoke(TargetBean, new Object[]{setterValue});
                }
            }
        } catch (Exception e) {
            throw new Exception("设置参数信息发生异常", e);
        }
        return TargetBean;
    }

    private static String getGetMethodName(String fieldName) {
        fieldName = replaceFirstCharToUpper(fieldName);
        return "get" + fieldName;
    }

    private static String getSetMethodName(String fieldName) {
        fieldName = replaceFirstCharToUpper(fieldName);
        return "set" + fieldName;
    }

    private static String replaceFirstCharToUpper(String fieldName) {
        char[] chars = new char[1];
        chars[0] = fieldName.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            fieldName = fieldName.replaceFirst(temp, temp.toUpperCase());
        }
        return fieldName;
    }

    public static void main(String[] args) throws Exception {
        Movies movies = new Movies();
        movies.setName("aaa");

        MovieVo movieVo = (MovieVo) getBean(new MovieVo(), movies);
        System.out.println(movieVo);
    }

}
