package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by ibm on 2017/3/9.
 * 依赖注入助手类
 *  先通过BeanHelper获取所有Bean Map (是一个Map<class<?>,Object>)结构，记录了类与对象的映射关系，
 *  然后遍历这个映射关系，分别取出Bean类与Bean实例，进而通过反射获取类中的所有成员变量，继续遍历这些成员变量
 *  在循环中怕买断当前成员变量是否带有inject注解，若带有注解，则从Bean Map 中根据Bean类取出Bean实例，最后通过
 *  ReflectionUtil.setField 方法来修改当前成员变量的值。
 */
public final class IocHelper {

    static{
        // 获取所有的bean类与Bean实例之间的映射关系(简称Bean Map)
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap ();
        if(CollectionUtil.isNotEmpty ( beanMap )){
            // 遍历Bean Map
            for(Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet ()){
                //从BeanMap 中获取Bean类 与Bean 实例
                Class<?> beanClass = beanEntry.getKey ();
                Object beanInstance = beanEntry.getValue ();
                //获取Bean类定义的所有成员变量(简称 Bean Field)
                Field[] beanFields = beanClass.getDeclaredFields ();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    //遍历 Bean Field
                    for(Field beanField :beanFields){
                        //判断当前 Bean Field 是否带有 Inject 注解
                        if(beanField.isAnnotationPresent ( Inject.class )){
                             //在 bean map 中 获取 bean field 的实例
                            Class<?> beanFieldClass = beanField.getType ();
                            Object beanFieldInstance = beanMap.get ( beanFieldClass );
                            if(beanFieldInstance != null){
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setFieldf ( beanInstance,beanField,beanFieldInstance );
                            }
                        }
                    }
                }
            }
        }
    }
}
