/*
 */

package com.tz.basis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public final class ConfigUtil {

	static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

	public static String defaultConfDir = "./classes/";

	public static Reader getFileReader(String fileNm) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(getFileInputStream(fileNm));
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("error: " + e.getMessage());
		}
		return reader;
	}

	public static InputStream getFileInputStream(String fileNm) {
		try {
			if (new File(fileNm).exists()) {
				return new FileInputStream(fileNm);
			} else if (new File(defaultConfDir + fileNm).exists()) {
				return new FileInputStream(defaultConfDir + fileNm);
			} else {
				return ConfigUtil.class.getClassLoader().getResourceAsStream(fileNm);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			logger.error("error: " + e.getMessage());
		}
		return null;
	}

	public static String getConfPath(String fileNm) {
		try {
			if (new File(fileNm).exists()) {
				return fileNm;
			} else if (new File(defaultConfDir + fileNm).exists()) {
				return defaultConfDir + fileNm;
			} else {
				return ConfigUtil.class.getClassLoader().getResource(fileNm).getPath();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("error: " + e.getMessage());
		}
		return null;
	}

	public static Properties getJsonProperty(String fileNm) {
		final Properties appProperty = new Properties();
		Reader reader = null;
		try {
			reader = getFileReader(fileNm);
			final JSONObject json = (JSONObject) new JSONParser().parse(reader);
			loadJson(appProperty, "", json);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (final IOException e) {
				e.printStackTrace();
				logger.error("error: " + e.getMessage());
			}
		}
		return appProperty;
	}

	public static void loadJson(Properties appProperty, String parent, JSONObject json) {
		for (final Iterator<?> iterator = json.keySet().iterator(); iterator.hasNext();) {
			final String key = (String) iterator.next();
			if (json.get(key) instanceof JSONObject) {
				loadJson(appProperty, key, (JSONObject) json.get(key));
			} else {
				appProperty.put(parent + "." + key, json.get(key));
			}
		}
	}

	public static Properties getProperty(String fileNm) {
		final Properties appProperty = new Properties();
		try {
			final Properties props = new Properties();
			props.load(getFileReader(fileNm));
			for (final Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				final String key = (String) en.nextElement();
				final String value = props.getProperty(key);
				appProperty.setProperty(key, value);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("error: " + e.getMessage());
		}
		return appProperty;
	}

	public static Properties getProperties(String fileNm, String ext) {
		final Properties appProperties = new Properties();
		try {
			final File file = new File(getConfPath(fileNm));
			final File afile[] = file.listFiles();
			for (int i = 0; i < afile.length; i++) {
				if (afile[i].getName().endsWith("." + ext)) {
					final Properties props = new Properties();
					props.load(new FileInputStream(afile[i].getAbsoluteFile().toString()));
					for (final Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
						final String key = (String) en.nextElement();
						final String value = props.getProperty(key);
						appProperties.setProperty(key, value);
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("error: " + e.getMessage());
		}
		return appProperties;
	}

	public static Properties getProperties(String path) {
		return getProperties(path, "conf");
	}

	public static void setLogbackConfig(String filePath) {
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			final JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			context.reset();
			configurator.doConfigure(ConfigUtil.getConfPath(filePath));
		} catch (final JoranException je) {
			je.printStackTrace();
			logger.error("error: " + je.getMessage());
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}

	public static String getConfig(String configFile, String path) {
		String rslt = "";
		try {
			String str = "";
			final Reader reader = getFileReader(configFile);
			try {
				int intValueOfChar;
				while ((intValueOfChar = reader.read()) != -1) {
					str += (char) intValueOfChar;
				}
				reader.close();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			JsonObject conf = (JsonObject) new JsonParser().parse(str);
			String pathArry[] = path.split("/");
			for (int i = 0; i < pathArry.length - 1; i++) {
				conf = (JsonObject) new JsonParser().parse(conf.get(pathArry[i]).toString());
			}
			rslt = conf.get(pathArry[pathArry.length - 1]).getAsString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return rslt;
	}
}
