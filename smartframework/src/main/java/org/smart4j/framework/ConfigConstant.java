package org.smart4j.framework;

/**
 * Created by ibm on 2017/3/7.
 */
public interface  ConfigConstant {

    String CONFIG_FILE = "smart.properties";

    String JDBC_DRIVER = "smart.framework.jdbc.driver";
    String JDBC_URL = "smart.framework.jdbc.url";
    String JDBC_USERNAME = "smart.framework.jdbc.username";
    String JDBC_PASSWORD = "smart.framework.jdbc.password";

    String APP_BASE_PACKAGE = "smart.framework.app.base_package";
    String APP_JSP_PATH = "smart.framework.app.jsp_path";
    String APP_ASSET_PATH = "smart.framework.app.asset_path";

    //上传文件最大限制
    String APP_UPLPAD_LIMIT = "smart.framework.app.upload_limit";
}
