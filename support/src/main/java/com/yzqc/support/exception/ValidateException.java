package com.yzqc.support.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ValidateException extends RuntimeException {

    private Map<String, String> validationMap = new HashMap<String, String>();

    public ValidateException(String field, String validateMsg) {
        addValidateMsg(field, validateMsg);
    }

    public ValidateException(String message, String field, String validateMsg) {
        super(message);
        addValidateMsg(field, validateMsg);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

    public ValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public void addValidateMsg(String field, String message) {
        validationMap.put(field, message);
    }

    public void clearValidateMsg() {
        validationMap.clear();
    }

    public Map<String, String> getValidationMap() {
        return validationMap;
    }

    public void setValidationMap(Map<String, String> validationMap) {
        this.validationMap = validationMap;
    }
}
