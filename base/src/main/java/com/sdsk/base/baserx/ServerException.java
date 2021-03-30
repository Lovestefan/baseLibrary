package com.sdsk.base.baserx;

/**
 * des:服务器请求异常
 * Created by xsf
 * on 2016.09.10:16
 */
public class ServerException extends Exception{

    public String code;
    public String msg;

    public ServerException(String code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
