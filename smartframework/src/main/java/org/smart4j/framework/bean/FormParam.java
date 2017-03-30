package org.smart4j.framework.bean;

/**
 * 封装表单参数
 * Created by ibm on 2017/3/29.
 */
public class FormParam {
    private String fileName;
    private Object fileValue;

    public FormParam(String fileName, Object fileValue) {
        this.fileName = fileName;
        this.fileValue = fileValue;
    }

    public String getFileName() {
        return fileName;
    }

    public Object getFileValue() {
        return fileValue;
    }
}
