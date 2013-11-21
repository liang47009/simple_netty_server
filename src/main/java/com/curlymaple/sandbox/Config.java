package com.curlymaple.sandbox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {
	private static Properties config = null;
	public static final String IP = "ip";
	public static final String PORT = "port";
	public static String CROSSDOMAIN;

	static {
		try {
			config = new Properties();
			config.load(ClassLoader
					.getSystemResourceAsStream("config.properties"));
			BufferedInputStream bis = new BufferedInputStream(
					ClassLoader.getSystemResourceAsStream("crossdomain.xml"));
			byte[] b = new byte[bis.available()];
			bis.read(b);
			CROSSDOMAIN = new String(b);
			Logger.getLogger(Config.class).info(CROSSDOMAIN);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getConfig() {
		return config;
	}

	public static String getProperty(String key) {
		if (config != null) {
			return config.getProperty(key);
		}
		return null;
	}
}