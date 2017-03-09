package org.smart4j.chapter2.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ibm on 2017/3/6.
 * 数据库操作助手类
 */
public class DatabaseHelper {
    //日志
    private static final Logger logger = LoggerFactory.getLogger ( DatabaseHelper.class );

    //DButils
    private static final QueryRunner QUERY_RUNNER;

    //ThreadLocal connection
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    //数据库连接池
    private static final BasicDataSource DATA_SOURCE;

    ////定义常用量
    //private static final String DRIVER;
    //private static final String URL;
    //private static final String USERNAME;
    //private static final String PASSWORD;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner (  );

        Properties conf = PropsUtil.loadProps ( "config.properties" );
        String driver = conf.getProperty ( "jdbc.driver" );
        String url = conf.getProperty ( "jdbc.url" );
        String username = conf.getProperty ( "jdbc.username" );
        String password = conf.getProperty ( "jdbc.password" );

        DATA_SOURCE = new BasicDataSource ();
        DATA_SOURCE.setDriverClassName ( driver );
        DATA_SOURCE.setUrl ( url );
        DATA_SOURCE.setUsername ( username );
        DATA_SOURCE.setPassword ( password );

        //try {
        //    Class.forName ( DRIVER );
        //} catch (ClassNotFoundException e) {
        //    //驱动启动不成时加入log
        //    logger.error ( "can not load jdbc driver", e );
        //}
    }

    //获取数据库连接
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get ();
        if(conn == null){
            try{
            //conn = DriverManager.getConnection ( URL, USERNAME, PASSWORD );
            conn = DATA_SOURCE.getConnection ();
        }catch  (SQLException e){
            logger.error ( "get connection failure", e );
            throw new RuntimeException ( e );
        } finally {
            CONNECTION_HOLDER.set ( conn );
            }
        }
        return conn;
    }

    ////关闭数据库连接
    //public static void closeConnection() {
    //    Connection conn = CONNECTION_HOLDER.get ();
    //    if (conn != null) {
    //        try {
    //            conn.close ();
    //        } catch (SQLException e) {
    //            logger.error ( "close connection failure", e );
    //            throw new RuntimeException ( e );
    //        }finally{
    //            CONNECTION_HOLDER.remove ();
    //        }
    //    }
    //}

    /**
     * 查询实体列表
     * */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList;
        try{
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query ( conn,sql,new BeanListHandler<T>(entityClass),params );
        }catch(SQLException e){
            logger.error("query entity list failure", e);
            throw new RuntimeException ( e );
        }
            // finally {
        //    closeConnection ();
        //}
        return entityList;
    }

    /**
     * 查询实体
     * */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        try{
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query ( conn,sql,new BeanHandler<T> (entityClass  ),params );
        }catch(SQLException e){
            logger.error ( "query entity failure",e );
            throw new RuntimeException ( e );
        }
        //finally{
        //    closeConnection ();
        //}
        return entity;
    }
    /**
     * 执行查询语句
     * 多表联查 返回一个list
     * */
    public static List<Map<String,Object>> executeQuery(String sql,Object... params){
        List<Map<String,Object>> result;
        try{
            Connection conn = getConnection ();
            result = QUERY_RUNNER.query ( conn,sql,new MapListHandler (),params );
        }catch(SQLException e){
            logger.error("execute query failure",e);
            throw new RuntimeException ( e );
        }
        //finally{
        //    closeConnection ();
        //}
        return result;
    }

    /**
     * 执行更新语句，提供一个通用的(包括, update .insert , delete)
     * */
    public static int executeUpdate(String sql,Object... params){
        int row = 0;
        try{
            Connection conn = getConnection();
            row = QUERY_RUNNER.update ( conn,sql,params );
        }catch(SQLException e){
            logger.error("execute update failure",e);
            throw new RuntimeException ( e );
        }
        //finally{
        //    closeConnection ();
        //}
        return row;
    }
    /**
     * 插入实体
     * */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object>fieldMap){
        if(CollectionUtil.isEmpty ( fieldMap )){
            logger.error("can not insert entity , fieldMap is empty");
            return false;
        }
        String sql = "insert into "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder ( "(" );
        StringBuilder values = new StringBuilder ( "(" );
        for(String fieldName : fieldMap.keySet()){
            columns.append ( fieldName ).append ( "," );
            values.append ( "?," );
        }
        columns.replace ( columns.lastIndexOf ( "," ),columns.length (),")" );
        values.replace ( values.lastIndexOf ( "," ),values.length (),")" );
        sql += columns +"values"+values;

        Object[] params = fieldMap.values ().toArray ();
        return executeUpdate ( sql,params ) == 1;
    }

    /**
     * 更新实体
     * */
    public static <T> boolean updateEntity(Class<T> entityClass,long id, Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty ( fieldMap )){
            logger.error("can not insert entity , fieldMap is empty");
            return false;
        }
        String sql = "update "+getTableName ( entityClass )+" set ";
        StringBuilder columns = new StringBuilder (  );
        for(String fieldName :fieldMap.keySet ()){
            columns.append(fieldName).append ( "=?," );
        }
        sql += columns.substring ( 0,columns.lastIndexOf ( "," ))+" where id = ? " ;
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll ( fieldMap.values () );
        paramList.add(id);
        Object[] params = paramList.toArray ();

        return executeUpdate(sql,params) == 1;
    }
    /**
     * 删除实体
     * */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){

        String sql = "delete from "+ getTableName ( entityClass )+" where id = ?";
        return executeUpdate ( sql,id ) == 1;
    }

    private static  String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName ();
    }

    /**
     * 执行sql文件
     * */
    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        System.out.print ( is );
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (Exception e) {
            logger.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }




}
