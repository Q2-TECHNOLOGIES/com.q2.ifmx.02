/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Administrator
 */
public class RespMessage {
    private String CHANNEL_ID;
    private String BIN_NO;
    private String USER_REF_NO;
    private String RESPONSE_TIME;
    private String SERVICE_CODE;
    private String AUTH_TOKEN;
    private String MESSAGE_KEY;
    private String ACTION;
    private String OFFICER_ID;
    private String REJECT_REASON;
    private String CODE_STATUS;
    private String DESC_STATUS;
    private String ORIGINAL_RESPONSE_MSG;

    public String getORIGINAL_RESPONSE_MSG() {
        return ORIGINAL_RESPONSE_MSG;
    }

    public void setORIGINAL_RESPONSE_MSG(String ORIGINAL_RESPONSE_MSG) {
        this.ORIGINAL_RESPONSE_MSG = ORIGINAL_RESPONSE_MSG;
    }
    
    

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getBIN_NO() {
        return BIN_NO;
    }

    public void setBIN_NO(String BIN_NO) {
        this.BIN_NO = BIN_NO;
    }

    public String getUSER_REF_NO() {
        return USER_REF_NO;
    }

    public void setUSER_REF_NO(String USER_REF_NO) {
        this.USER_REF_NO = USER_REF_NO;
    }

    public String getRESPONSE_TIME() {
        return RESPONSE_TIME;
    }

    public void setRESPONSE_TIME(String RESPONSE_TIME) {
        this.RESPONSE_TIME = RESPONSE_TIME;
    }

    public String getSERVICE_CODE() {
        return SERVICE_CODE;
    }

    public void setSERVICE_CODE(String SERVICE_CODE) {
        this.SERVICE_CODE = SERVICE_CODE;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public void setAUTH_TOKEN(String AUTH_TOKEN) {
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

    public String getMESSAGE_KEY() {
        return MESSAGE_KEY;
    }

    public void setMESSAGE_KEY(String MESSAGE_KEY) {
        this.MESSAGE_KEY = MESSAGE_KEY;
    }

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }

    public String getOFFICER_ID() {
        return OFFICER_ID;
    }

    public void setOFFICER_ID(String OFFICER_ID) {
        this.OFFICER_ID = OFFICER_ID;
    }

    public String getREJECT_REASON() {
        return REJECT_REASON;
    }

    public void setREJECT_REASON(String REJECT_REASON) {
        this.REJECT_REASON = REJECT_REASON;
    }

    public String getCODE_STATUS() {
        return CODE_STATUS;
    }

    public void setCODE_STATUS(String CODE_STATUS) {
        this.CODE_STATUS = CODE_STATUS;
    }

    public String getDESC_STATUS() {
        return DESC_STATUS;
    }

    public void setDESC_STATUS(String DESC_STATUS) {
        this.DESC_STATUS = DESC_STATUS;
    }
    
    
    
}
