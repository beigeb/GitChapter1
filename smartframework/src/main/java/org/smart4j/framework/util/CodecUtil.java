package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by ibm on 2017/3/20.
 * Codec 编码解码器
 * 编码与解码操作工具类
 */
public final class CodecUtil {
    private static final Logger logger = LoggerFactory.getLogger ( CodecUtil.class );

    /**
     * 将url编码
     */
    public static String encodeURL(String source){
        String target;
        try{
            target = URLEncoder.encode ( source,"UTF-8" );
        }catch(Exception e){
                logger.error("encode failure ",e);
                throw new RuntimeException ( e );
        }
        return target;
    }
    /**
     * 将url解码
     */
    public static String decodeURL(String source){
        String target;
        try{
            target = URLDecoder.decode ( source,"UTF-8" );
        }catch (Exception e){
            logger.error("decode failure" , e);
            throw new RuntimeException ( e );
        }
        return target;
    }
}
