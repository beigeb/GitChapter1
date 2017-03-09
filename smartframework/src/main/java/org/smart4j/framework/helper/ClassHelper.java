package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ibm on 2017/3/8.
 * 类操作助手类
 */
public final class ClassHelper {

    /**
     * 定义类的集合
     * */
    private static final Set<Class<?>> CLASS_SET;

    static{
        String basePackage = ConfigHelper.getAppBasePackage ();
        CLASS_SET = ClassUtil.getClassSet ( basePackage );
    }
    /**
     * 获取应用包名下的所有类
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下的所有service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>> (  );
        for(Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent ( Controller.class )){
                classSet.add ( cls );
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下所有controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSst = new HashSet<Class<?>> (  );
        for(Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent ( Controller.class )){
                classSst.add ( cls );
            }
        }
        return classSst;
    }

    /**
     * 获取应用包名下所有的bean类(包括： service controller )
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<Class<?>> (  );
        beanClassSet.addAll ( getServiceClassSet () );
        beanClassSet.addAll ( getControllerClassSet () );
        return beanClassSet;
    }

}