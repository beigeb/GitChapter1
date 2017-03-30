package org.smart4j.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json 工具类
 * Created by ibm on 2017/3/20.
 */
public final class JsonUtil {

        private static final Logger logger = LoggerFactory.getLogger ( JsonUtil.class );

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将pojo转换为Json
     */
    public static <T> String toJson(T obj){
        String json;
        try{
            //转换成jackjson
            json = OBJECT_MAPPER.writeValueAsString ( obj );
        }catch(Exception e){
            logger.error("convert pojo to json failure ", e);
            throw  new RuntimeException ( e );
        }
        return json;
    }

    /**
     *  将 json 转换成 pojo
     */
    public static  <T> T fromJson(String json ,Class<T> type){
        T pojo;
        try{
            pojo = OBJECT_MAPPER.readValue ( json,type );
        }catch(Exception e){
            logger.error("convert json to pojo failure" , e);
            throw new RuntimeException ( e );
        }
        return pojo;
    }

}
