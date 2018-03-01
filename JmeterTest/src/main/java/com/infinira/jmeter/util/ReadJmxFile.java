package com.infinira.jmeter.util;

import java.io.File;

import com.infinira.jmeter.runner.JmeterRunner;
import com.infinira.jmeter.service.JmeterService;

public class ReadJmxFile {

	public void readAllJmxFiles(){
		loadAllJmxFiles(JmeterService.getInstance().getJmxFolder());
	}
	public void loadAllJmxFiles(String path) {
		File directory = new File(path);
        //get all the files from a directory
        File[] jmxFileList = directory.listFiles();
       
        for (File jmxfile : jmxFileList) {
        	if(jmxfile.getName().endsWith(".jmx")) {
        		new JmeterRunner().runJMXFile(jmxfile.getAbsolutePath());
        	}
           
        }
	}
}
