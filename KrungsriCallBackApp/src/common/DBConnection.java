/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author reyhan.rahmansyah
 */
public class DBConnection {
    
    private final static Logger log = LogManager.getLogger(DBConnection.class); 
    // Connect DB
    public Connection conn;
    public Connection getConnection(PropertiesLoader pl) {
         // additional handling issue immediate error and end scheduler, when restarting DB server (add login timeout and logic looping to retry)
        String db_url = "jdbc:jtds:sqlserver://"+
                    pl.db_host+":"+
                    pl.db_port+"/"+
                    pl.db_name+
                    ";loginTimeout="+ pl.db_login_timeout+";";
        
        for (int i = 0; i < pl.db_retry_count; i++) {
            try{
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // Connect ke yang ada di app.properties pake pl dari class main
                //conn = DriverManager.getConnection("jdbc:sqlserver://"+pl.db_host+":"+pl.db_port+";databaseName="+pl.db_name,pl.db_username,pl.db_password+";encrypt="+pl.encrypt+"trustServerCertificate="+pl.trustServer);
                //return conn;
                Class.forName("net.sourceforge.jtds.jdbc.Driver");            
                conn = DriverManager.getConnection(db_url,pl.db_username,pl.db_password);
                break;
            } catch(ClassNotFoundException e){
                log.error(e.getMessage());
                System.out.println(e.getMessage());
                return null;
            } catch(SQLException e){
                log.error("Failed to connect DB. Retry count:" + (i + 1));
                log.error(e.getMessage());
                System.out.println(e.getMessage());
                if (i < pl.db_retry_count - 1) {
                    try {
                        log.info("Wait to retry DB connection");
                        Thread.sleep(pl.db_delay_sec * 1000); } catch (InterruptedException ignored) {}
                } else {
                    log.error("Fail to connect DB after " + pl.db_retry_count + " retries.");
                    System.err.println("Fail to connect after " + pl.db_retry_count + " retries.");
                    return null;
                }
            }
        }
        
        return conn;
    }
}
