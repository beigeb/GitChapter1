package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by ibm on 2017/3/10.
 * 请求转发器
 * loadOnSTARTUP
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
       //初始化相关的helper类
        HelperLoader.init ();
        //获取ServletContext对象（用于注册Servlet） 【在servlet 初始化时 会传入ServletConfig 。该对象允许
        // 访问两个内容， ：初始化参数和ServletContext】
        ServletContext servletContext = servletConfig.getServletContext ();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration ( "jsp" );
        jspServlet.addMapping ( ConfigHelper.getAppAssetPath () + "*" );
        //注册处理静态资源的servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration ( "default" );
        defaultServlet.addMapping ( ConfigHelper.getAppAssetPath () + "*" );
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求方法和路径
        String requestMethod = request.getMethod ().toLowerCase ();
        String requestPath = request.getPathInfo ();

        if(requestPath.equals ( "/favicon.ico" )){
            return ;
        }

        //获取Action处理器
        Handler handler = ControllerHelper.getHandler (requestMethod, requestPath );
        if(handler != null) {
            // 获取Controller类及其bean实例
            Class<?> controllerClass = handler.getControllerClass ();
            Object controllerBean = BeanHelper.getBean ( controllerClass );

            Param param;
            if(UploadHelper.isMultipart ( request )){
                param = UploadHelper.createParam ( request );
            }else{
                param = RequestHelper.createParam ( request );
            }

            Object result;
            Method actionMethod = handler.getActionMethod ();
            if(param.isEmpty ()){
                result = ReflectionUtil.invokeMethod ( controllerBean,actionMethod );
            }else{
                result = ReflectionUtil.invokeMethod ( controllerBean,actionMethod,param );
            }
            //  判断是不是右面类创建的对象
            if(result instanceof View){
                handleViewResult((View) result,request,response);
            }else if(request instanceof  Data){
                handleDataResult((Data) result,response);
            }
    //        //创建请求参数对象
    //        Map<String,Object> paramMap = new HashMap<String,Object>();
    //        Enumeration<String> paramNames = request.getParameterNames ();
    //        while(paramNames.hasMoreElements ()){
    //            String paramName = paramNames.nextElement ();
    //            String paraValue = request.getParameter ( paramName );
    //            paramMap.put ( paramName,paraValue );
    //        }
    //        String body = CodecUtil.decodeURL( StreamUtil.getString(request.getInputStream ()));
    //        if(StringUtil.isNotEmpty ( body )){
    //            String [] params = StringUtil.splitString(body , "&");
    //            if(ArrayUtil.isNotEmpty ( params )){
    //                for(String param : params){
    //                    String []array = StringUtil.splitString(param ," =");
    //                    if(ArrayUtil.isNotEmpty ( array ) && array.length  == 2){
    //                        String paramName = array[0];
    //                        String paramValue = array[1];
    //                        paramMap.put ( paramName,paramValue );
    //                    }
    //                }
    //            }
    //        }
    //        Param param = new Param ( paramMap );
    //        //调用Action 方法
    //        Method actionMethod = handler.getActionMethod ();
    //        Object result = ReflectionUtil.invokeMethod ( controllerBean,actionMethod,param );
    //        //处理Action方法的返回值
    //        //这个对象是否是这个特定类或者是它的子类的一个实例
    //        // result 是否是 view 的实例/或是这个类
    //        if(result instanceof View){
    //            //返回JSP 页面
    //            View view = (View) result;
    //            String path = view.getPath ();
    //            if(StringUtil.isNotEmpty ( path )){
    //                //是否由 "/" 开头
    //                if(path.startsWith ( "/" ));
    //                    response.sendRedirect(request.getContextPath ()+path);
    //            }else{
    //                Map<String,Object> model = view.getModel ();
    //                for(Map.Entry<String,Object> entry : model.entrySet ()){
    //                    request.setAttribute ( entry.getKey (),entry.getValue () );
    //                }
    //                request.getRequestDispatcher ( ConfigHelper.getAppAssetPath()+path ).forward ( request,response );
    //            }
    //        }else if( result instanceof  Data){
    //            //返回Json 数据
    //            Data data = (Data) result;
    //            Object model = data.getModel ();
    //            if(model != null){
    //                //使客户端区分不同种类的数据
    //                response.setContentType ( "application/json" );
    //                response.setCharacterEncoding ( "UTF-8" );
    //                PrintWriter writer = response.getWriter ();
    //                String json = JsonUtil.toJson(model);
    //                writer.write ( json );
    //                //显示的将数据写出
    //                writer.flush ();
    //                writer.close ();
    //            }
    //        }
        }
    }

    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        String path = view.getPath ();
        if(StringUtil.isNotEmpty ( path )){
            if(path.startsWith ( "/" )){
                response.sendRedirect ( request.getContextPath () + path );
            }else{
                Map<String,Object> model = view.getModel ();
                for(Map.Entry<String,Object> entry : model.entrySet ()){
                    request.setAttribute ( entry.getKey (),entry.getValue () );
                }
                request.getRequestDispatcher ( ConfigHelper.getAppJspPath () + path ).forward ( request,response );
            }
        }
    }

    private void handleDataResult(Data data,HttpServletResponse response) throws IOException{
        Object model =data.getModel ();
        if(model != null){
            response.setContentType ( "application/json" );
            response.setCharacterEncoding ( "UTF-8" );
            PrintWriter writer = response.getWriter ();
            String json = JsonUtil.toJson ( model );
            writer.write ( json );
            writer.flush ();
            writer.close ();
        }
    }


}
