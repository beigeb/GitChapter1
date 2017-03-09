package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ibm on 2017/3/9.
 * 反射工具类
 */
public final class ReflectionUtil {
    private static final Logger logger = LoggerFactory.getLogger ( ReflectionUtil.class );

    /**
     * 创建实例
     */

    public static Object newInstance(Class<?> cls){
        Object instance ;
        try{
            instance = cls.newInstance ();
        }catch(Exception e){
            logger.error("new instance failure ",e);
            throw new RuntimeException ( e );
        }
        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args){
        Object result;
        try{
            //设置变量可访问。 private 编程 public 表示可以忽略访问权限的限制，直接调用。
            method.setAccessible ( true );
            //调用方法。方法名字作为参数传入
            result = method.invoke ( obj,args );
        }catch(Exception e){
            logger.error("invoke method failure",e);
            throw new RuntimeException ( e );
        }
        return result;
    }

    /**
     * 设置成员变量的值
     */
    public static void setFieldf(Object obj,Field field,Object value){
        try{
            field.setAccessible ( true );
            field.set ( obj,value );
        }catch(Exception e){
            logger.error("set field failure",e);
            throw new RuntimeException ( e );
        }
    }
}
