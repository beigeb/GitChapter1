package org.smart4j.chapter2.service;


import org.apache.log4j.Logger;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/3/6.
 * 提供客户数据服务类
 */
public class CustomerService {

    private  static final Logger logger = org.apache.log4j.Logger.getLogger ( CustomerService.class );
    ////定义常用量
    //private static final String DRIVER;
    //private static final String URL;
    //private static final String USERNAME;
    //private static final String PASSWORD;
    //
    //static{
    //    Properties conf = PropsUtil.loadProps ( "config.properties" );
    //    DRIVER = conf.getProperty ( "jdnc.driver" );
    //    URL = conf.getProperty ( "jdbc.url" );
    //    USERNAME = conf.getProperty ( "jdbc.username" );
    //    PASSWORD = conf.getProperty ( "jdbc.password" );
    //
    //    try{
    //        Class.forName ( DRIVER );
    //    }catch(ClassNotFoundException e){
    //        //驱动启动不成时加入log
    //        logger.error("can not load jdbc driver",e);
    //    }
    //}

    //获取客户列表
    public List<Customer> getCustomerList(){
        //todo
        //Connection conn =  DatabaseHelper.getConnection ();
        //try{
            //List<Customer> customerList = new ArrayList<Customer> (  );
            String sql = "select * from customer";
            return DatabaseHelper.queryEntityList ( Customer.class,sql );

            //PreparedStatement stmt = conn.prepareStatement ( sql );
            //ResultSet rs = stmt.executeQuery ();
            //while(rs.next ()){
            //    Customer customer = new Customer ();
            //    customer.setId ( rs.getLong ( "id" ) );
            //    customer.setName(rs.getString ( "name" ));
            //    customer.setContact ( rs.getString (  "contact") );
            //    customer.setTelephone ( rs.getString ( "telephone" ) );
            //    customer.setEmail ( rs.getString ( "email" ) );
            //    customer.setTelephone ( rs.getString ( "remark" ) )
        // ;
            //    customerList.add ( customer );
            //}
            //return customerList;
        //}
        //catch(SQLException e){
        //    logger.error ( "execute sql failur " ,e );
        //}
        //finally {
        //    DatabaseHelper.closeConnection ( conn );
        //}
    }

    //获取用户
    public Customer getCustomer(long id){
        //todo
        String sql = "select * from customer where id = ?";
        return DatabaseHelper.queryEntity ( Customer.class,sql,id );
    }
    //创建客户
    public boolean createCustomer(Map<String,Object> fieldMap){
        //todo

        return DatabaseHelper.insertEntity ( Customer.class, fieldMap ) ;
    }

    //更新客户
    public boolean updateCustomer(long id,Map<String,Object> filedMap){

        return DatabaseHelper.updateEntity ( Customer.class,id,filedMap );
    }

    //删除客户
    public boolean deleteCustomer(long id){

        return DatabaseHelper.deleteEntity ( Customer.class,id );
    }


}
