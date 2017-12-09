package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import prototype.BasicFlow;
import salesforce.cpq.catalog.RecreateSyncPublish;


public class Test {
	private final static String STARTLOG=
			"###################################################START LOG SESSION########################################################";
	private final static String ENDLOG=
			"###################################################END LOG SESSION########################################################\n\n\n";
	private static Logger log;
	private static Property config;

	//brutal way to turn off apache http client log
	//TODO: Find a better solution to filter that logs with log4j
	static{
        System.setProperty("current.date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(Level.OFF);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
    }


	public static void main(String[] args){
		try {
			config = Property.getInstance();
			PropertyConfigurator.configure(new FileInputStream(config.getProperty("log4j.config")));
		} catch (FileNotFoundException e) {
			log.error("Log4j property file not found: "+e.getMessage());
			return;
		}
		log = Logger.getLogger(Test.class);
		log.info(STARTLOG);
		new RecreateSyncPublish();
		log.info(ENDLOG);
	}
}

