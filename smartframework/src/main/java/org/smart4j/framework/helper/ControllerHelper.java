package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ibm on 2017/3/9.
 * 控制器助手类
 *
 */
public class ControllerHelper {

    /**
     * 用于存放请求与处理器的关系(简称Action Map)
     */
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler> (  );

    static{
        //获得所有的controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet ();
        if(CollectionUtil.isNotEmpty ( controllerClassSet )){
            //遍历这些controller类
            for(Class<?> controllerClass : controllerClassSet){
                //获取controller定义的方法
                Method[] methods = controllerClass.getDeclaredMethods ();
                if(ArrayUtil.isNotEmpty ( methods )){
                    //遍历这些controller类中的方法
                    for(Method method : methods){
                        //判断当前方法是否带有Action注解
                        if(method.isAnnotationPresent ( Action.class )){
                            //从Action 中 获得 URL映射规则
                            Action action = method.getAnnotation ( Action.class );
                            String mapping = action.value ();
                            //验证URL映射规则
                            //正则表达式
                            if(mapping.matches ( "\\w+:/\\w*" )){
                                String [] array = mapping.split ( ":" );
                                if(ArrayUtil.isNotEmpty ( array ) && array.length == 2){
                                    //获取请求方法与路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request ( requestMethod,requestPath );
                                    Handler handler = new Handler ( controllerClass,method );
                                    //初始化 acton Map
                                    ACTION_MAP.put ( request,handler );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = new Request ( requestMethod,requestPath );
        return ACTION_MAP.get ( request );
    }
}
