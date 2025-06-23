/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.RespMessage;
import org.apache.commons.codec.binary.Base64;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger; 

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 *
 * @author reyhan.rahmansyah
 */
public class SendAPI {
    //final static Logger log = Logger.getLogger(SendAPI.class);
    private final static Logger log = LogManager.getLogger(SendAPI.class); 
    
    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    public static String generateAuthorization(String UserName, String Password){
        byte[] encodedBytes = Base64.encodeBase64((UserName+":"+Password).getBytes());
        return new String(encodedBytes);
    }
    
    public RespMessage SendHttpPostAPI(PropertiesLoader pl, String reqJsonMsg) throws IOException{
        JSONMessageBuilder jMsgBuilder = new JSONMessageBuilder();
        String authorization = generateAuthorization(pl.middelware_username, pl.middleware_password);
        try{
            System.setProperty("javax.net.ssl.trustStore","C:/Program Files/Java/jdk1.8.0_251/jre/lib/security/cacerts");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            
            URL url = new URL (pl.callbackurl);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setConnectTimeout(pl.api_conn_timeout);
            con.setReadTimeout(pl.api_req_msg_timeout);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("Authorization", "Basic "+authorization);
            con.setDoOutput(true);
            
            OutputStream os = con.getOutputStream();
            byte[] input = reqJsonMsg.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            RespMessage respMessage = jMsgBuilder.createResponseJSONObject(response.toString());
            System.out.println("Response Message : "+response.toString());
            return respMessage;
        } catch(IOException e){
            //ERROR HANDLING FOR READ TIMEOUT AND CONNECTION TIMEOUT.
            RespMessage respMessage = new RespMessage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            respMessage.setRESPONSE_TIME(sdf.format(new Date()));
            log.error(e.getMessage());
            if("Read timed out".equals(e.getMessage())){
                respMessage.setCODE_STATUS("9999"); //read timeout error code
                respMessage.setDESC_STATUS("ERROR");
                return respMessage;
            } else if("java.net.SocketTimeoutException: connect timed out".equals(e.getMessage())){
                respMessage.setCODE_STATUS("8888"); //connection timeout error code
                respMessage.setDESC_STATUS("ERROR");
                return respMessage;
            } else{
                respMessage.setCODE_STATUS("7777"); //unknown error code
                respMessage.setDESC_STATUS("ERROR");
                return respMessage;
            }
        } catch(KeyManagementException e){
            RespMessage respMessage = new RespMessage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            respMessage.setRESPONSE_TIME(sdf.format(new Date()));
            respMessage.setCODE_STATUS("7777"); //unknown error code
            respMessage.setDESC_STATUS("ERROR");
            log.error(e.getMessage());
             return respMessage;
        } catch(NoSuchAlgorithmException e){
            RespMessage respMessage = new RespMessage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            respMessage.setRESPONSE_TIME(sdf.format(new Date()));
            respMessage.setCODE_STATUS("7777"); //unknown error code
            respMessage.setDESC_STATUS("ERROR");
            log.error(e.getMessage());
             return respMessage;
        }
    }
}
