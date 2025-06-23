/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.ReqMessage;
import model.RespMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author reyhan.rahmansyah
 */
public class JSONMessageBuilder {
    public String createJSONReqMessage(PropertiesLoader pl, ReqMessage reqMsg, int msgCounter, String request_time){
        try{
            String user_ref_no = reqMsg.getMESSAGE_KEY()+"_"+msgCounter;
            
            Common cmn = new Common();
            String authToken = cmn.generateAuthToken("0000", pl.middleware_security_word, request_time, user_ref_no, "AML_CALLBACK");
            
            JSONObject obj = new JSONObject();
            obj.put("channel_id", "AML");
            obj.put("bin_no", "0000");
            obj.put("user_ref_no", user_ref_no);
            obj.put("request_time", request_time);
            obj.put("service_code", "AML_CALLBACK");
            obj.put("auth_token", authToken);
            obj.put("message_key", reqMsg.getMESSAGE_KEY());
            obj.put("action", reqMsg.getSTATUS_NAME());
            obj.put("officer_id", reqMsg.getUSER_IDENTIFIER());
            obj.put("reject_reason", reqMsg.getREJECT_REASON());
            return obj.toString();
        } catch(JSONException e){
            System.out.println(e);
        }
        return null;
    }
    
    public RespMessage createResponseJSONObject(String responseMessage){
        RespMessage respMessage = new RespMessage();
        JSONObject respJSONObject = new JSONObject(responseMessage);
        respMessage.setCHANNEL_ID(respJSONObject.getString("channel_id"));
        respMessage.setBIN_NO(respJSONObject.getString("bin_no"));
        respMessage.setUSER_REF_NO(respJSONObject.getString("user_ref_no"));
        respMessage.setRESPONSE_TIME(respJSONObject.getString("response_time"));
        respMessage.setSERVICE_CODE(respJSONObject.getString("service_code"));
        respMessage.setAUTH_TOKEN(respJSONObject.getString("auth_token"));
        respMessage.setMESSAGE_KEY(respJSONObject.getString("message_key"));
        respMessage.setACTION(respJSONObject.getString("action"));
        respMessage.setOFFICER_ID(respJSONObject.getString("officer_id"));
        respMessage.setREJECT_REASON(respJSONObject.getString("reject_reason"));
        respMessage.setCODE_STATUS(respJSONObject.getString("code_status"));
        respMessage.setDESC_STATUS(respJSONObject.getString("desc_status"));
        respMessage.setORIGINAL_RESPONSE_MSG(responseMessage);
        return respMessage;
    }
}
