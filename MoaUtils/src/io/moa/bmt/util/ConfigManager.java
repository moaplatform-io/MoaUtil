package io.moa.bmt.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class ConfigManager {

	private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
	
	public static final String ARG_LOG_CONF_FILE_PATH ="logConfPath";
	Properties property = new Properties();

	public ConfigManager(String propertyFilePath) throws IOException {
		FileInputStream fis = new FileInputStream(propertyFilePath);
		property.load(fis);
		fis.close();

		String logConfFilePath = this.getStrConf(ARG_LOG_CONF_FILE_PATH, "");

		if (logConfFilePath != "") {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

			try {
				JoranConfigurator logConfigurator = new JoranConfigurator();
				logConfigurator.setContext(context);
				context.reset();
				logConfigurator.doConfigure(logConfFilePath);

			} catch (JoranException e) {
				e.printStackTrace();
			}

			StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		}
		
		
		for (String key : property.stringPropertyNames()) {
			log.debug(key + " = " + property.getProperty(key));
		}
	}

	public int getIntConf(String key, int defaultVal) {

		try {
			return Integer.parseInt(property.getProperty(key));
		} catch (Exception e) {
			log.error("Fail to get a int property: " + key, e);

			return defaultVal;
		}
	}

	public double getDoubleConf(String key, double defaultVal) {

		try {
			return Double.parseDouble(property.getProperty(key));
		} catch (Exception e) {
			log.error("Fail to get a double property: " + key, e);

			return defaultVal;
		}
	}

	public String getStrConf(String key, String defaultVal) {
		if (property.getProperty(key) != null)
			return property.getProperty(key);

		return defaultVal;
	}

}
