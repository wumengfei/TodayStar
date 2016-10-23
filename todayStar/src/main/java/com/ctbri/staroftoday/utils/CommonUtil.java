package com.ctbri.staroftoday.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Murphy on 16/8/30.
 */
public class CommonUtil {
    public static int generateValidateCode() {
        Random random = new Random();
        int nextInt;
        do {
            nextInt = random.nextInt(999999);
        } while (nextInt < 100000);
        return nextInt;
    }

    public static boolean isMobileNO(String mobiles) {
        boolean flag;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            // LOG.error("验证邮箱地址错误", e);
            flag = false;
        }

        return flag;
    }

    /**
     * 验证密码是否合乎规则
     *
     * @param password
     * @return
     */
    public static boolean validatePassWord(String password) {
        boolean flag;
        try {
            String check = "^[A-Za-z0-9]{6,16}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(password);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
