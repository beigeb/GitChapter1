package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.ClassUtil;

/**
 * Created by ibm on 2017/3/9.
 * 加载相应Helper类
 */
public final class HelperLoader {

    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class


        };
        for (Class<?> cls : classList){
            ClassUtil.loadClass ( cls.getName () ,true);
        }
    }
}
