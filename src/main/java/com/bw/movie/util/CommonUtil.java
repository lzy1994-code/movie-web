package com.bw.movie.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @program: health-service
 * @description: 公共工具类
 * @author: Lzy
 * @create: 2019-03-01 19:22
 **/
public class CommonUtil {
    /**
     * 获取分页查询下标
     *
     * @param page
     * @param count
     * @return
     */
    public static int getPageIndex(int page, int count) {
        return (page - 1) * count;
    }


    public static String upload(MultipartFile[] files, String str1, String str2) {
        String savePath;
        List<String> headPaths = new ArrayList<>();
        String headPath;
        if (files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String nowTime = sdf2.format(new Date());
                String newFileName = df.format(new Date()) + "." + suffix;
                savePath = String.format(str1, nowTime, newFileName);
                headPath = String.format(str2, nowTime, newFileName);
                headPaths.add(headPath);
                //上传图片
                try {
                    File f = new File(savePath);
                    if (!f.exists()) {
                        f.getParentFile().mkdirs();
                    }
                    file.transferTo(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return String.join(",", headPaths);
        }
        return null;
    }

    /**
     * 生成邀请码工具类
     *
     * @param length
     * @return
     */
    public static String getInvitationCode(int length) {
        String val = "";
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            val += String.valueOf((char) (new Random().nextInt(26) + 65));
        }
        return val;
    }

    /**
     * 生成随机串(邮箱验证码)
     *
     * @param length
     * @return
     */
    public static String createCheckCode(int length) {
        String chars = "0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 好评率计算工具类
     *
     * @param badNum    差评数
     * @param praiseNum 好评数
     * @return
     */
    public static String getPraiseData(int badNum, int praiseNum) {
        int sumNum = praiseNum + badNum;
        if (sumNum == 0) {
            return "0.00%";
        }
        float num = (float) praiseNum * 100 / sumNum;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(num);
        return format + "%";
    }

    /**
     * 处理参数有效性
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int convert(String str, int defaultValue) {
        if (str == null) return defaultValue;
        String s = str.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("undefined") || s.equalsIgnoreCase("null")) return defaultValue;
        try {
            return Integer.parseInt(s);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * 生成随机串
     *
     * @param length
     * @return
     */
    public static String createNonceStr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }


    public static void main(String[] args) {
        String invitationCode = getInvitationCode(10);
        System.out.println(invitationCode);
    }

}
