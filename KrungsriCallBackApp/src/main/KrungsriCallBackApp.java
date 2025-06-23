/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import common.Common;
import common.DBConnection;
import common.PropertiesLoader;
import common.JSONMessageBuilder;
import common.SendAPI;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.ReqMessage;
import model.RespMessage;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger; 

/**
 *
 * @author reyhan.rahmansyah
 */
public class KrungsriCallBackApp {
    //final static Logger log = Logger.getLogger(KrungsriCallBackApp.class);
    private final static Logger log = LogManager.getLogger(KrungsriCallBackApp.class); 

    public static void main(String[] args)throws MalformedURLException, IOException, Exception {
        log.info("### KRUNGSRI CALLBACK APP RUNNING ###");
        System.out.println("### KRUNGSRI CALLBACK APP RUNNING ###");
        
        log.info("Reading Properties file "+args[0]);
        System.out.println("Read on Properties file "+args[0]);
        
        PropertiesLoader pl = new PropertiesLoader(args[0]);
        ReqMessage objReqMsg = new ReqMessage();
        Common cmn = new Common();
        
        DBConnection objDbConn = new DBConnection();
        Connection dbConn =  objDbConn.getConnection(pl);
        
        if (dbConn == null){
            System.out.println("Unable to connect to DB");
            log.error("Unable to connect to DB");
        }else{
            try{
                log.info("Connected to DB "+pl.db_name);
                //String query = "SELECT * FROM IMPL_REALTIME_ALERTS WHERE (MIDDLEWARE_RESPONSE_STATUS IS NULL OR MIDDLEWARE_RESPONSE_STATUS <> '000000') and (SEND_COUNTER IS NULL OR SEND_COUNTER <= 3)";
                String query = "SELECT * FROM IMPL_REALTIME_ALERTS WHERE MIDDLEWARE_RESPONSE_STATUS IS NULL";
                Statement stmt = dbConn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                SendAPI sendAPI = new SendAPI();
                JSONMessageBuilder msgBuilder = new JSONMessageBuilder();
                
                int msgCounter = 0;
                SimpleDateFormat reqDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                while(rs.next()){
                    objReqMsg = new ReqMessage();
                    objReqMsg.setALERT_CLOSED_DATE(rs.getString("ALERT_CLOSED_DATE"));
                    objReqMsg.setMESSAGE_KEY(rs.getString("MESSAGE_KEY"));
                    objReqMsg.setSTATUS_NAME(rs.getString("STATUS_NAME"));
                    objReqMsg.setSTATUS_IDENTIFIER(rs.getString("STATUS_IDENTIFIER"));
                    objReqMsg.setISSUE_NOISSUE(rs.getString("ISSUE_NOISSUE"));
                    objReqMsg.setUSER_IDENTIFIER(rs.getString("USER_IDENTIFIER"));
                    objReqMsg.setREJECT_REASON(rs.getString("REJECT_REASON"));
                    
                    String request_time = reqDateFormat.format(new Date());

                    String jsonReqMessage = msgBuilder.createJSONReqMessage(pl, objReqMsg, msgCounter, request_time);
                    log.info("Request Message JSON : "+jsonReqMessage);
                    System.out.println("Request Message JSON : "+jsonReqMessage);
                    
                    log.info("Sending JSON Message to Krungsri API...");
                    System.out.println("Sending JSON Message to Krungsri API...");
                    
                    RespMessage respObject = sendAPI.SendHttpPostAPI(pl, jsonReqMessage);
                    
                    log.info("Response Code :"+respObject.getCODE_STATUS());
                    System.out.println("Response Code :"+respObject.getCODE_STATUS());
                    
                    if("9999".equals(respObject.getCODE_STATUS())){
                        log.info("Response Message : Read Timeout");
                        System.out.println("Response Message : Read Timeout");
                    } else if("8888".equals(respObject.getCODE_STATUS())){
                        log.info("Response Message : Connection Timeout");
                        System.out.println("Response Message : Connection Timeout");
                    } else if("7777".equals(respObject.getCODE_STATUS())){
                        log.info("Response Message : Unknown error");
                        System.out.println("Response Message : Unknown error");
                    } else{
                        System.out.println("Response Message JSON : "+respObject.getORIGINAL_RESPONSE_MSG());
                        log.info("Response Message JSON : "+respObject.getORIGINAL_RESPONSE_MSG());
                    }
                    
                    /*
                    Important Notes :
                    1. Response Time status code is 9999. Error code defined by callback apps not from krungsri api
                    2. Response Connection timeout status code is 8888. Error code defind by callback apps not from krungsri api
                    3. Response null is unknown issues
                    */
                    if("ERROR".equals(respObject.getDESC_STATUS())){ //performing retry if response desc is ERROR
                        log.info("Retrying sending JSON Message to Krungsri API...");
                        System.out.println("Retrying sending JSON Message to Krungsri API...");
                        Integer retry_counter = 1;
                        while(retry_counter <= pl.api_retry_count){
                            try{
                                System.out.println("Retry "+retry_counter);
                                log.info("Retry "+retry_counter);
                                
                                System.out.println("Wating for "+pl.api_retry_delay+" Milisecond to retry...");
                                log.info("Wating for "+pl.api_retry_delay+" Milisecond to retry...");
                                
                                Thread.sleep(pl.api_retry_delay);
                                
                                System.out.println("Sending retry JSON Message to Krungsri API...");
                                log.info("Sending retry JSON Message to Krungsri API...");
                                respObject = sendAPI.SendHttpPostAPI(pl, jsonReqMessage);
                                if(!"ERROR".equals(respObject.getDESC_STATUS())){
                                    log.info("Retry process success...");
                                    System.out.println("Retry process success...");
                                    
                                    log.info("Response Message JSON : "+respObject.getORIGINAL_RESPONSE_MSG());
                                    System.out.println("Response Message JSON : "+respObject.getORIGINAL_RESPONSE_MSG());
                                    break; //exiting looping because the response is not error. retry no longer needed
                                } else{
                                    log.info("Response Code :"+respObject.getCODE_STATUS());
                                    System.out.println("Response Code :"+respObject.getCODE_STATUS());
                                    if("9999".equals(respObject.getCODE_STATUS())){
                                        log.info("Response Message : Read Timeout");
                                        System.out.println("Response Message : Read Timeout");
                                    } else if("8888".equals(respObject.getCODE_STATUS())){
                                        log.info("Response Message : Connection Timeout");
                                        System.out.println("Response Message : Connection Timeout");
                                    } else if("7777".equals(respObject.getCODE_STATUS())){
                                        log.info("Response Message : Unknown error");
                                        System.out.println("Response Message : Unknown error");
                                    }
                                    
                                    System.out.println("Retry process...");
                                    log.info("Retry process...");
                                    cmn.updateSendCounter(pl, objReqMsg.getMESSAGE_KEY());
                                    retry_counter++;
                                }
                            } catch(InterruptedException e){
                                log.error("Retry process interrupted!!!");
                                System.out.println(e);
                            }
                        }
                    }
                    
                    if(respObject != null){
                        if("ERROR".equals(respObject.getDESC_STATUS())){
                            log.info("Message Key "+rs.getString("MESSAGE_KEY")+" failed to send to Krungsri API due to error with code "+respObject.getCODE_STATUS()+" after retry "+pl.api_retry_count+" times");
                            System.out.println("Message Key "+rs.getString("MESSAGE_KEY")+" failed to send to Krungsri API due to error with code "+respObject.getCODE_STATUS()+" after retry "+pl.api_retry_count+" times");
                            cmn.updateSendCounter(pl, objReqMsg.getMESSAGE_KEY());
                            cmn.updateResponseStatus(pl, objReqMsg.getMESSAGE_KEY(), respObject.getCODE_STATUS(), respObject.getRESPONSE_TIME());
                            log.info("Success updated data to DB");
                        } else{
                            cmn.updateSendCounter(pl, respObject.getMESSAGE_KEY());
                            cmn.updateResponseStatus(pl, respObject.getMESSAGE_KEY(), respObject.getCODE_STATUS(), respObject.getRESPONSE_TIME());
                            log.info("Success updated data to DB");
                        }
                    }
                    msgCounter++;
                }
                log.info("Total Processed Message = "+msgCounter);
            } catch(SQLException e){
                System.out.println(e);
                log.error(e);
            }
        }
        System.out.println("### KRUNGSRI CALLBACK APP FINISHED RUNNING ###");
        log.info("### KRUNGSRI CALLBACK APP FINISHED RUNNING ###");
    }
}
