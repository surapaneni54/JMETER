package com.infinira.jmeter.util;


import org.apache.log4j.Logger;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
public class ServiceUtil {
     
    private static volatile ServiceUtil serviceutil;
    private static ResourceBundle messages;
    private static final String JMETER_MSG_FILE = "Jmetermessage";
    private static Logger logger ;
    
    
    public static ServiceUtil getInstance() {
		if (serviceutil == null) {
			synchronized(ServiceUtil.class) {
				if (serviceutil == null) {
					serviceutil = new ServiceUtil ();  // lazy loading                 
				}
			}
		}
	return serviceutil;
	}
    
	private ServiceUtil() { 
        read();
	} 
    private void  read() {
        try {
            messages = ResourceBundle.getBundle(JMETER_MSG_FILE, Locale.getDefault());
        }catch (Throwable th) {
             throw new RuntimeException(MessageFormat.format("File {0}.properties not found in Class path.", JMETER_MSG_FILE), th);
        }
    }
    static {
        try {
            logger = Logger.getLogger(ServiceUtil.class.getName());
        } catch(Throwable th) {
            throw new RuntimeException("Error while importing log properties file.", th);
        }
    }
 
    public  void throwException(String msgId,Throwable th, Object... parameter) {
        String msgWithparameter =getMessage(msgId, parameter);
        if (th != null) {
        	logger.error("'"+msgWithparameter+"': '"+th+"'");
        } else {
        	logger.error("'"+msgWithparameter);
        }
        
        throw new RuntimeException(msgWithparameter, th);
       
    }
    
    
    private static String getMessage(String msgId, Object... params) {
        String msg = "";
        try {
            msg = messages.getString(msgId);
            msg = MessageFormat.format(msg, params);
        } catch (Throwable th) {
            msg = "Invalid message key <"+msgId+"> specified in the Resource Bundle";
            //throw new RuntimeException(MessageFormat.format("The key {0} is not"+" valid ", msgId), th);
        }
        return msg;
    }
    
}    

