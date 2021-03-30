package com.sdsk.base.basebean;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
public class Base2Respose<T> implements Serializable {

    /**
     * meta : {"success":true,"message":"ok","code":200}
     * data : {}
     */

    public MetaBean meta;
    public T data;


    public boolean success() {
        return meta.code == 200 && meta.success;
    }

    public static class MetaBean {
        /**
         * success : true
         * message : ok
         * code : 200
         */

        public boolean success;
        public String message;
        public int code;

    }

}
