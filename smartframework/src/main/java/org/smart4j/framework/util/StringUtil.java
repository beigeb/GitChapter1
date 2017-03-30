package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ibm on 2017/3/6.
 * 字符串工具类
 */
public class StringUtil {

    public static final  String SEPARATOR = String.valueOf ( (char)29 );

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
    /**
     * 分割固定格式的字符串
     */
    public static String[] splitString(String str, String separator) {
        return StringUtils.splitByWholeSeparator(str, separator);
    }
}
