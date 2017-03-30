package org.smart4j.test.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ibm on 2017/3/27.
 */
public class MyThreadLocal<T> {

    private Map<Thread,T>  container = Collections.synchronizedMap ( new HashMap<Thread, T> (  ) );

    public void set(T value){
        //   currentThread 返回当前运行的线程
        container.put ( Thread.currentThread (),value );
    }

    public T get(){
        Thread thread = Thread.currentThread ();
        T value = container.get ( thread );
        if(value == null && !container.containsKey ( thread )){
            value = initialValue();
            container.put ( thread,value );
        }
        return value;
    }

    public void remove(){
        container.remove ( Thread.currentThread () );
    }

    public T initialValue(){
        return null;
    }
}
