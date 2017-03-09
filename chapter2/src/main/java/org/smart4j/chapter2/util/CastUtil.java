package org.smart4j.chapter2.util;

/**
 * Created by ibm on 2017/3/6.
 * 转型操作工具类
 */
public class CastUtil {

    //转为String类型
    public static String castString(Object obj){
        return CastUtil.castString ( obj,"" );
    }

    //转为String型(提供默认值)
    public static String castString(Object obj,String defaultvalue ){
        return obj != null ? String.valueOf ( obj ) : defaultvalue;
    }

    //转为double型
    public static double castDouble(Object obj){
        return CastUtil.castDouble ( obj,0 );
    }

    //转为boolean型(提供默认值)
    public static double castDouble(Object obj,double defaultvalue){
        double doublevalue = defaultvalue;
        if(obj != null){
            String strValue = castString ( obj );
                if(StringUtil.isNotEmpty(strValue)){
                    try{
                        doublevalue = Double.parseDouble ( strValue );
                    }catch(NumberFormatException e){
                        doublevalue = defaultvalue;
                    }
                }
            }
        return doublevalue;
    }

    //转为log型
    public static long castLong(Object obj){
        return CastUtil.castLong ( obj,0 );
    }

    //转为long类型(提供默认值)
    public static long castLong(Object obj,long defaultValue){
        long longValue = defaultValue;
        if(obj != null){
            String strValue = castString ( obj );
                if(StringUtil.isNotEmpty(strValue)){
                       try{
                         longValue = Long.parseLong(strValue);
                      }catch(NumberFormatException e){
                         longValue=defaultValue;
                    }
                }
            }
            return longValue;
        }

        //转为int类型
    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    //准尉int型(提供默认值)
    public static int castInt(Object obj,int defaultValue){
        int intValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch(NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    //转为boolean型
    public static boolean castBoolean(Object obj){
        return CastUtil.castBoolean ( obj,false );
    }

    //转为boolean(提供默认值)
    public static boolean castBoolean(Object obj,boolean defaultValue){
        boolean booleanValue = defaultValue;
        if(obj != null){
            booleanValue = Boolean.parseBoolean ( castString ( obj ) );
        }
        return booleanValue;
    }
}


