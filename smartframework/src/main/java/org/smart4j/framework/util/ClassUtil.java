package org.smart4j.framework.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by ibm on 2017/3/8.
 * 类操作的工作类
 */
public final class ClassUtil {

    private static final Logger logger = LoggerFactory.getLogger (ClassUtil.class  );

    /**
     * 获取类加载器
     * */
    public static ClassLoader getClassLoader(){

        //获取当前线程的claaloader
        return Thread.currentThread ().getContextClassLoader ();
    }

    /**
     * 加载类
     * */
    public static Class<?> loadClass(String className,boolean isInitialized){
        //需要提供类名 与 是否初始化的标志。 初始化是指是否执行了
        //静态代码块
        Class<?> cls ;
        try{
            cls = Class.forName ( className,isInitialized,getClassLoader () );
        }catch(ClassNotFoundException e){
            logger.error("load class failure ",e);
            throw  new RuntimeException ( e );
        }
        return cls;
    }

    /**
     * 加载类（默认将初始化类）
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 获取指定包名下的所有类
     * */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>> (  );
        try{
            //获取数据接口
            //replace 把 . 替换成 /
            Enumeration<URL> urls = getClassLoader ().getResources ( packageName.replace ( "." ,"/") );
            //枚举对象是否还有元素
            while(urls.hasMoreElements ()){
                //可以从含有多个元素的数据结构中的获得下一个元素
                URL url = urls.nextElement ();
                if(url != null){
                    //getProtocol:http
                    String protocol = url.getProtocol ();
                    if(protocol.equals ( "file" )){
                        String packagePath = url.getPath ().replaceAll ( "%20"," " );
                        addClass(classSet,packagePath,packageName);
                    }else if(protocol.equals ( "jar" )){
                        //获得一个远程连接对象
                        JarURLConnection jarURLConnection =(JarURLConnection) url.openConnection ();
                        if(jarURLConnection != null){
                            //创建jar文件对象
                            JarFile jarFile = jarURLConnection.getJarFile ();
                            if(jarFile != null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries ();
                                while(jarEntries.hasMoreElements ()){
                                    JarEntry jarEntry = jarEntries.nextElement ();
                                    String jarEntryName = jarEntry.getName ();
                                    //方法返回的测试该字符串是否以指定后缀"class"结束
                                    if(jarEntryName.endsWith ( ".class" )){
                                        //String c = a.substring (inta,intb) a截取从inta到intb的字符串赋值给c;
                                        String className = jarEntryName.substring ( 0,jarEntryName.lastIndexOf ( "." )).replaceAll ( "/","." ) ;
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            logger.error("get class set failure",e);
            throw new RuntimeException ( e );
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files = new File (packagePath).listFiles ( new FileFilter () {
            public boolean accept(File file) {
                return (file.isFile() && file.getName ().endsWith ( ".class" ) || file.isDirectory ());
            }
        } );
        for(File file :files){
            String fileName = file.getName ();
            if(file.isFile ()){
                String className = fileName.substring ( 0,fileName.lastIndexOf ( "." ) );
                if(StringUtil.isNotEmpty ( packageName )){
                    className = packageName + "." + className;
                }
                    doAddClass(classSet,className);
            } else{
            String subPackagePath = fileName;
            if(StringUtil.isNotEmpty ( packagePath )){
                subPackagePath = packagePath + "." + subPackagePath;
                }
                String subPackageName = fileName;
            if(StringUtil.isNotEmpty ( packageName )){
                subPackageName = packageName + "." + subPackageName;
                }
                    addClass ( classSet,subPackagePath,subPackageName );
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass ( className,false );
        classSet.add ( cls );
    }
}
