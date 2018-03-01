package com.infinira.jmeter.runner;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import com.infinira.jmeter.service.JmeterService;
import com.infinira.jmeter.util.ServiceUtil;

public class JmeterRunner {
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public void runJMXFile(String jmxFileName) {
		
		StandardJMeterEngine jmeter = new StandardJMeterEngine();
		File jmxFile = new File(jmxFileName);
		htmlReportPath(jmxFileName);
		
		try {
			SaveService.loadProperties();
		} catch (IOException ex) {
			throw new RuntimeException("problem with jmeter property file ", ex);
		}

		HashTree testPlanTree = null;

		try {
			testPlanTree = SaveService.loadTree(jmxFile);
		} catch (Exception ex) {
			throw new RuntimeException("problem with jmx file " + jmxFile, ex);
		}

		
		Summariser summer = null;
		String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
		if (summariserName.length() > 0) {
			summer = new Summariser(summariserName);
		}
		Date date = new Date();
	    String csvFile = JmeterService.getInstance().getResultCSV()+FilenameUtils.removeExtension(jmxFile.getName())+dateFormat.format(date)+".csv";
		ResultCollector csvReporter = new ResultCollector(summer);
		
		csvReporter.setFilename(csvFile);
		testPlanTree.add(testPlanTree.getArray()[0], csvReporter);
		ReportGenerator rptGen = null;
		try {
			rptGen = new ReportGenerator(csvFile, csvReporter);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jmeter.configure(testPlanTree);
		jmeter.run();
		
		
		try {
			rptGen.generate();
		} catch (Throwable th) {
			throw new RuntimeException("Problem in Generating Html Report", th);
		}	
		System.out.println("success");		
	}
	
	private void htmlReportPath(String jmxFileName) {
		Date date = new Date();

		String folderName = JmeterService.getInstance().getHtmlReportPath() + jmxFileName.substring(jmxFileName.lastIndexOf("\\"), jmxFileName.lastIndexOf(".")) + dateFormat.format(date);
		File file = new File(folderName);
        if (!file.isDirectory()) {
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
                ServiceUtil.getInstance().throwException("JMETER-0103", null, folderName);
            }
        }

		JMeterUtils.setProperty("jmeter.reportgenerator.exporter.html.property.output_dir", folderName);

	}
}
