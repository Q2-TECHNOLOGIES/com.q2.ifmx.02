/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author reyhan.rahmansyah
 */
public class ReqMessage {
    private String ALERT_CLOSED_DATE;
    private String MESSAGE_KEY;
    private String STATUS_NAME;
    private String STATUS_IDENTIFIER;
    private String ISSUE_NOISSUE;
    private String USER_IDENTIFIER;
    private String REJECT_REASON;
    private String MIDDLEWARE_RESPONSE_STATUS;
    private String RESPONSE_DATE;
    private String SEND_COUNTER;

    public String getALERT_CLOSED_DATE() {
        return ALERT_CLOSED_DATE;
    }

    public void setALERT_CLOSED_DATE(String ALERT_CLOSED_DATE) {
        this.ALERT_CLOSED_DATE = ALERT_CLOSED_DATE;
    }

    public String getMESSAGE_KEY() {
        return MESSAGE_KEY;
    }

    public void setMESSAGE_KEY(String MESSAGE_KEY) {
        this.MESSAGE_KEY = MESSAGE_KEY;
    }

    public String getSTATUS_NAME() {
        return STATUS_NAME;
    }

    public void setSTATUS_NAME(String STATUS_NAME) {
        this.STATUS_NAME = STATUS_NAME;
    }

    public String getSTATUS_IDENTIFIER() {
        return STATUS_IDENTIFIER;
    }

    public void setSTATUS_IDENTIFIER(String STATUS_IDENTIFIER) {
        this.STATUS_IDENTIFIER = STATUS_IDENTIFIER;
    }

    public String getISSUE_NOISSUE() {
        return ISSUE_NOISSUE;
    }

    public void setISSUE_NOISSUE(String ISSUE_NOISSUE) {
        this.ISSUE_NOISSUE = ISSUE_NOISSUE;
    }

    public String getUSER_IDENTIFIER() {
        return USER_IDENTIFIER;
    }

    public void setUSER_IDENTIFIER(String USER_IDENTIFIER) {
        this.USER_IDENTIFIER = USER_IDENTIFIER;
    }

    public String getREJECT_REASON() {
        return REJECT_REASON;
    }

    public void setREJECT_REASON(String REJECT_REASON) {
        this.REJECT_REASON = REJECT_REASON;
    }

    public String getMIDDLEWARE_RESPONSE_STATUS() {
        return MIDDLEWARE_RESPONSE_STATUS;
    }

    public void setMIDDLEWARE_RESPONSE_STATUS(String MIDDLEWARE_RESPONSE_STATUS) {
        this.MIDDLEWARE_RESPONSE_STATUS = MIDDLEWARE_RESPONSE_STATUS;
    }

    public String getRESPONSE_DATE() {
        return RESPONSE_DATE;
    }

    public void setRESPONSE_DATE(String RESPONSE_DATE) {
        this.RESPONSE_DATE = RESPONSE_DATE;
    }

    public String getSEND_COUNTER() {
        return SEND_COUNTER;
    }

    public void setSEND_COUNTER(String SEND_COUNTER) {
        this.SEND_COUNTER = SEND_COUNTER;
    }
}
