package org.smart4j.chapter2.util;
/**
 * Created by ibm on 2017/3/6.
 * 字符串工具类
 */
public class StringUtil {

    //判断字符串是否为空
    public static boolean isEmpty(String str){
        if(str != null){
            //去掉字符串首尾空格
            str = str.trim ();
        }
        return StringUtil.isEmpty ( str );
    }

    //判断字符串是否非空
    public static boolean isNotEmpty(String str){
        return !isEmpty ( str );
    }
}
