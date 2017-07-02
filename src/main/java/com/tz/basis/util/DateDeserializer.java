package com.tz.basis.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeserializer implements JsonDeserializer<Date> {
	private static final Logger logger = LoggerFactory.getLogger(DateDeserializer.class);

	@Override
	public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String date = element.getAsString();
		//String locale = element.get
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yy hh:mm a");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			logger.debug("Failed to parse Date due to: "+e.toString());
			return null;
		}
	}
}
