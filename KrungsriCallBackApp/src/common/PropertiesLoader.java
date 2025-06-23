/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author reyhan.rahmansyah
 */
public class PropertiesLoader {
    private Properties prop = null;
    public String db_host;
    public String db_port;
    public String db_name;
    public String db_username;
    public String db_password;
    public String db_login_timeout;
    public Integer db_retry_count;
    public Integer db_delay_sec;
    public String callbackurl;
    public String encrypt;
    public String trustServer;
    
    public String middelware_username;
    public String middleware_password;
    public String middleware_security_word;
    
    public Integer api_conn_timeout;
    public Integer api_req_msg_timeout;
    public Long api_retry_delay;
    public Integer api_retry_count;
        
    public PropertiesLoader(String prop_file) throws Exception{
        prop = new Properties();
        Decryptor dec = new Decryptor();
        InputStream input = null;
        try {
            input = new FileInputStream(prop_file);
            prop.load(input);
        } catch (IOException io) {
            System.out.println("Unable to load properties file. "+io);
        }
        
        db_host= prop.getProperty("DB_HOST");
        db_port= prop.getProperty("DB_PORT");
        db_name= prop.getProperty("DB_NAME");
        db_username= prop.getProperty("DB_USERNAME");
        db_password= prop.getProperty("DB_PASSWORD");// Seharusnya di-encrypt dulu abis itu di-decrypt dulu baru di-load
        db_password = dec.decrypt(db_password);
        // additional handling issue immediate error and end scheduler, when restarting DB server
        db_login_timeout= prop.getProperty("DB_LOGIN_TIMEOUT");
        db_retry_count = Integer.parseInt(prop.getProperty("DB_RETRY_COUNT"));
        db_delay_sec = Integer.parseInt(prop.getProperty("DB_DELAY_SEC"));

        // end of additional handling issue immediate error and end scheduler, when restarting DB server
        
        callbackurl= prop.getProperty("CALLBACK_URL");
        middelware_username= prop.getProperty("MIDDLEWARE_USERNAME");
        middleware_password= prop.getProperty("MIDDLEWARE_PASSWORD"); 
        middleware_password = dec.decrypt(middleware_password);
        middleware_security_word= prop.getProperty("MIDDLEWARE_SECURITY_WORD");
        encrypt = prop.getProperty("ENCRYPT");
        trustServer = prop.getProperty("TRUSTSERVER");
        
        api_conn_timeout= Integer.parseInt(prop.getProperty("API_CONNECTION_TIMEOUT"));
        api_req_msg_timeout= Integer.parseInt(prop.getProperty("API_REQUEST_MSG_TIMEOUT"));
        api_retry_delay= Long.valueOf(prop.getProperty("API_RETRY_DELAY"));
        api_retry_count = Integer.parseInt(prop.getProperty("API_RETRY_COUNT"));
    }
}
