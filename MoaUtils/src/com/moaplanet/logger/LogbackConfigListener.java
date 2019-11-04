// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogbackConfigListener.java

package com.moaplanet.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackConfigListener implements ServletContextListener {
	public static final String CONFIG_LOCATION_PARAM = "logbackConfigLocation";
	public static String location;
	public static String mode;
	public LogbackConfigListener() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = null;
		LoggerContext lc = null;
		InputStream is = null;
		sc = sce.getServletContext();
		org.slf4j.ILoggerFactory ilc = LoggerFactory.getILoggerFactory();
		try {

			if (location == null) {
				location = sc.getRealPath("WEB-INF");
				location = location.substring(0, location.length() - 8);
			}
			if (!(ilc instanceof LoggerContext)) {
				sc.log((new StringBuilder("Can not configure logback. ")).append(LoggerFactory.class)
						.append(" is using ").append(ilc.getClass()).append(" which is not a ")
						.append(LoggerContext.class).toString());
				return;
			}
			lc = (LoggerContext) ilc;
			String location = getLocation(sc, lc);
			if (location == null)
				return;
			sc.log((new StringBuilder("Configuring logback from config resource located at ")).append(location)
					.toString());
			is = openInputStream(sc, location);
			if (is == null) {
				sc.log((new StringBuilder(
						"Could not open logback config neither as servlet context-, nor as url-, nor as file system resource. Location: "))
								.append(location).toString());
				return;
			}
			configureLogback(sc, lc, is);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				is.close();
			} catch (IOException ex) {
				sc.log("Could not close logback config inputstream.", ex);
			}
		}
		return;
	}

	protected String getLocation(ServletContext sc, LoggerContext lc) {
		String location = sc.getInitParameter("logbackConfigLocation");
		String  mode=sc.getInitParameter("spring.profiles.active").toString();
		System.err.println("Moa Server mode:["+mode+"]");
		location = location.replace("#mode#", mode);
		LogbackConfigListener.mode=mode;
		return location;
	}

	protected InputStream openInputStream(ServletContext sc, String location) {
		InputStream is = null;
		if (location == null)
			return null;
		if (location.startsWith("WEB-INF"))
			try {
				String root = location;
				return new FileInputStream(
						new File((new StringBuilder(String.valueOf(root))).append("/").append(location).toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (location.startsWith("/")) {
			location = location.replace("#mode#", mode);
			System.err.println("LOG CLASS PATH::"+location);
			is=LogbackConfigListener.class.getClassLoader().getResourceAsStream(location);
			if(is==null) {
				System.err.println("is location is null:"+location);
				is = sc.getResourceAsStream(location);
				try{
					Thread.sleep(2000);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		else
			try {
				is = (new URL(location)).openStream();
			} catch (IOException ioexception) {
			}
		if (is == null)
			try {
				is = new FileInputStream(location);
			} catch (FileNotFoundException filenotfoundexception) {
			}
		return is;
	}

	protected void configureLogback(ServletContext sc, LoggerContext lc, InputStream is) {
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.stop();
		try {
			configurator.doConfigure(is);
			System.err.println(">>>"+is);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			}catch (Exception ex) {
				// TODO: handle exception
			}
			System.err.println("JoranException>>>"+is);

			sc.log("Logback configuration failed.", e);
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.stop();
	}

}
