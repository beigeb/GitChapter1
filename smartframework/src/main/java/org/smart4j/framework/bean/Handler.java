package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by ibm on 2017/3/9.
 * 封装Action 信息
 */
public class Handler {

    /**
     * controller 类
     *
     */
    private Class<?> controllerClass;

    /**
     * Action 方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass,Method actionMethod){
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
