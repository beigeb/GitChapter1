package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 *  切面代理
 * Created by ibm on 2017/3/24.
 */
public abstract class  AspectProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger ( AspectProxy.class );

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass ();
        Method method = proxyChain.getTargetMethod ();
        Object[] params = proxyChain.getMethodParams ();

        begin();
        try{
            if(intercept(cls,method,params)){
                before(cls,method,params);
                result = proxyChain.doProxyChain ();
                after(cls,method,params,result);
            }else{
                result = proxyChain.doProxyChain ();
            }
        }catch(Exception e){
            logger.error("proxy failure",e);
            error(cls,method,params,e);
            throw e;
        }finally{
            end();
        }

        return result;
    }

    /**
     * 开始
     */
    public void begin(){

    }

    /**
     * 拦截
     * @param cls
     * @param method
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean intercept(Class<?> cls,Method method,Object[] params)throws Throwable{
        return true;
    }

    /**
     *  在 ·· 之前
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    public void before(Class<?> cls,Method method,Object[] params)throws Throwable{

    }

    /**
     *  在 ·· 之后
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    public void after(Class<?> cls,Method method,Object[] params,Object result)throws Throwable{

    }

    /**
     *
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    public void error(Class<?> cls,Method method,Object[] params,Throwable e){

    }

    public void end(){

    }

}
