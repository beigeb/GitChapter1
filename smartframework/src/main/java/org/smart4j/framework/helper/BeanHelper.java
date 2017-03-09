package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ibm on 2017/3/9.
 *   助手类
 */
public final class BeanHelper {

    /**
     * 定义bean映射 (用于存放Bean类与Bean实例的映射关系)
     */
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>,Object>();

    static{
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet ();
        for(Class<?> beanClass : beanClassSet){
            Object obj = org.smart4j.framework.util.ReflectionUtil.newInstance ( beanClass );
            BEAN_MAP.put ( beanClass,obj );
        }
    }

    /**
     * 获取Bean 映射
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取bean 实例
     */
    //对可能出现的警告 类似强转 保持屏蔽
    @SuppressWarnings ( "unchecked" )
    public static <T> T getBean(Class<T> cls){
        //如果不包含
        if(!BEAN_MAP.containsKey ( cls )){
            throw new RuntimeException ( "can not get bean by class : "+ cls );
        }
        return (T) BEAN_MAP.get ( cls );
    }
}
