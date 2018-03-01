package com.infinira.jmeter.service;

import java.io.InputStream;
import java.util.Properties;

import org.apache.jmeter.util.JMeterUtils;

import com.infinira.jmeter.util.ServiceUtil;

public class JmeterService {
	private static final String PROPERTIES_FILE = "jmeter-config.properties";
	private static final String  JMETER_PROPERTY_FILE_LOCATION = "jmeterpropertyfile";
	private static final String JMETER_HOME = "jmeterhome";
	private static final String JMX_FILE_LOCATION = "jmxfile";
	private static final String JMX_FOLDER_LOCATION = "jmxfolder";
	private static final String RESULT_CSV="resultcsv";
	private static final String HTML_REPORT_PATH = "htmlReportPath";
	private static volatile JmeterService instance;

	private String jmeterpropertyFile;
	private String jmeterHome;
	private String jmxFile;
	private String jmxFolder;
	private String resultcsv;
	private String htmlReportPath;
	public static JmeterService getInstance() {
		if (instance == null) {
			synchronized (JmeterService.class) {
				if (instance == null) {
					instance = new JmeterService();
				}
			}
		}
		return instance;
	}
	private JmeterService() {
		init();
	}

	// loading properties
	private void init() {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
		} catch(Throwable th) {
			ServiceUtil.getInstance().throwException("JMETER-0102", null, PROPERTIES_FILE);
		}
		
		try {
            properties.load(inputStream);
        } catch(Throwable th) {
            ServiceUtil.getInstance().throwException("JMETER-0103", th, PROPERTIES_FILE);
        }
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable th) {}
			}
		}

		jmeterpropertyFile = properties.getProperty(JMETER_PROPERTY_FILE_LOCATION);
		validate(JMETER_PROPERTY_FILE_LOCATION,jmeterpropertyFile,PROPERTIES_FILE);
		jmeterHome = properties.getProperty(JMETER_HOME);
		validate(JMETER_HOME,jmeterHome,PROPERTIES_FILE);
		jmxFile = properties.getProperty(JMX_FILE_LOCATION);
		validate(JMX_FILE_LOCATION,jmxFile,PROPERTIES_FILE);
		jmxFolder = properties.getProperty(JMX_FOLDER_LOCATION);
		validate(JMX_FOLDER_LOCATION,jmxFolder,PROPERTIES_FILE);
		resultcsv =  properties.getProperty(RESULT_CSV);
		validate(RESULT_CSV,resultcsv,PROPERTIES_FILE);
		htmlReportPath = properties.getProperty(HTML_REPORT_PATH);
		validate(HTML_REPORT_PATH, htmlReportPath,PROPERTIES_FILE);
		
		JMeterUtils.loadJMeterProperties(getJmeterPropertyFile());
		JMeterUtils.setJMeterHome(getJmeterHome());
		JMeterUtils.initLocale();
		
	}

	private void validate(String property, String value, String PROPERTY_FILE) {
        if(value == null || value.equals("")) {
            ServiceUtil.getInstance().throwException("JMETER-0101", null,property,PROPERTY_FILE);
        } 

	}
	
	

	public String getJmeterPropertyFile() {
		return jmeterpropertyFile;
	}
	
	public String getJmeterHome() {
		return jmeterHome;
		
	}
	
	public String getJmxFile() {
		return jmxFile;
	}
	
	public String getJmxFolder() {
		return jmxFolder;
	}
	
	
	public String getResultCSV() {
		return resultcsv;
	}
	
	public String getHtmlReportPath() {
		return htmlReportPath;
	}
	
	
	
	
	
}
