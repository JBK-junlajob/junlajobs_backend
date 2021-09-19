package com.junlajobs_backend.exception;

public class UserException extends BaseException{

    public UserException(String code) {
        super("user."+code);
    }

    //user.login.request.null
    public static UserException loginRequestIsNull(){
        return new UserException("login.request.null");
    }

    //user.password.request.null
    public static UserException loginPasswordIsNull(){
        return new UserException("password.request.null");
    }

}
