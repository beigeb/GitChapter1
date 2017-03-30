package org.smart4j.framework.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件操作工具类
 * Created by ibm on 2017/3/30.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger (FileUtil.class);

    /**
     * 获取真实文件名 (自动去掉文件路径)
     */
    public static String getRealFileName(String fileName){
        return FilenameUtils.getBaseName ( fileName );
    }

    /**
     * 创建文件
     */
    public static File createFile(String filePath){
        File file;
        try{
            file = new File ( filePath );
            // 父类目录
            File parentDir = file.getParentFile ();
            if(!parentDir.exists ()){
                FileUtils.forceMkdir ( parentDir );
            }
        }catch(Exception e){
            logger.error("create file failure",e);
            throw new RuntimeException ( e );
        }
        return file;
    }

}
