package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理类
 * Created by ibm on 2017/3/24.
 */
public class ProxyManager {

    //不显示警告
    @SuppressWarnings ( "unchecked" )
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
        // cglib 创建代理
        return (T) Enhancer.create ( targetClass, new MethodInterceptor () {
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        } );
    }
}
