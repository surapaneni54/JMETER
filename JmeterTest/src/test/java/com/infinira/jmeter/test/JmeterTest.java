package com.infinira.jmeter.test;

import com.infinira.jmeter.util.ReadJmxFile;

public class JmeterTest {
	public static void main(String[] args) {
		
		ReadJmxFile readJmxFile = new ReadJmxFile();
		readJmxFile.readAllJmxFiles(); // Read all the jmx files
	}
}
