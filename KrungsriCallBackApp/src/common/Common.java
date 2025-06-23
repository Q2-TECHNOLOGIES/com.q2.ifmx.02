/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Administrator
 */
public class Common {
    public static void updateSendCounter(PropertiesLoader pl, String message_key){
        DBConnection dbConn = new DBConnection();
        Connection conn = dbConn.getConnection(pl);
        if(conn != null){
            try{
                String queryUpdate = "UPDATE IMPL_REALTIME_ALERTS SET SEND_COUNTER = SEND_COUNTER+1 WHERE MESSAGE_KEY = '"+message_key+"'";
                Statement stmt = conn.createStatement();
                Integer resultUpdate = stmt.executeUpdate(queryUpdate);
                
            } catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    
    public static void updateResponseStatus(PropertiesLoader pl, String message_key, String response_status, String response_date){
        try{
            DBConnection dbConn = new DBConnection();
            Connection conn = dbConn.getConnection(pl);

            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Date dtReponsedate = originalDateFormat.parse(response_date);
            String new_response_date = newDateFormat.format(dtReponsedate);

            if(conn != null){
                try{
                    String queryUpdate = "UPDATE IMPL_REALTIME_ALERTS SET MIDDLEWARE_RESPONSE_STATUS = '"+response_status+"', "
                            + "MIDDLEWARE_RESPONSE_DATE = '"+new_response_date+"' "
                            + "WHERE MESSAGE_KEY = '"+message_key+"'";
                    Statement stmt = conn.createStatement();
                    Integer resultUpdate = stmt.executeUpdate(queryUpdate);
                    
                } catch(SQLException e){
                    System.out.println(e);
                }
            }
        } catch(ParseException e){
            System.out.println(e);
        }
    }
    
    public static String toHexString(byte[] hash){
    // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32){
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
    
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException{
    // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String generateAuthToken(String bin_no, String security_word, String request_time, String user_ref_no, String service_code) {
        String SHA265SecurityWord = org.apache.commons.codec.digest.DigestUtils.sha256Hex(security_word);               
        String completeText = bin_no+SHA265SecurityWord+request_time+user_ref_no+service_code;
        completeText = org.apache.commons.codec.digest.DigestUtils.sha256Hex(completeText);       
        
        /*System.out.println("Generating Auth Token with values:");
        System.out.println("BIN NO: "+bin_no);
        System.out.println("SECURITY_WORD: "+security_word);
        System.out.println("REQUEST TIME: "+request_time);
        System.out.println("USER REF NO : "+user_ref_no);
        System.out.println("SERVICE CODE : "+service_code);
        System.out.println("AUTH TOKEN : "+completeText);*/
                
        return completeText;
    }
}
