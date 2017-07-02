package com.tz.basis.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.tz.basis.user.UserInformation;

public class CommonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	private static final String url_check_regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static Pattern patt = Pattern.compile(url_check_regex);
	private static DecimalFormat krwFormat = new DecimalFormat("#,###,###,##0");
	private static DecimalFormat usdFormat = new DecimalFormat("#,###,###,##0.00");
	private static double coffeeValue = 2.7;
	private static double beerValue = 1.8;
	private static double burgerValue = 4.0;
	
	public static String getURLWithContextPath(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme() + "://" + request.getServerName());
		if(request.getServerPort()!=80 && request.getServerPort()!=443){
			sb.append(":" + request.getServerPort());
		}
		if(request.getContextPath().equals("/")){
			
		} else{
			sb.append(request.getContextPath());
		}
		CONTEXT_PATH = sb.toString();
		return sb.toString();
	}

	private static String CONTEXT_PATH;
	public static String getStoredContextPath(){
		return CONTEXT_PATH;
	}
	
	
	public static boolean isTextEmpty(String source) {
		if (source == null || source.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static double getRoundValueByCurrency(String currency, double value) {
		if ("KRW".equalsIgnoreCase(currency)) {
			return Math.round(value);
		} else if ("USD".equalsIgnoreCase(currency)) {
			return Math.round(value/.001)*.001;
		} else {
			return value;
		}
	}

	public static boolean isUrl(String source) {
		if (source == null || source.trim().length() == 0) {
			return false;
		}
		try {
			Matcher matcher = patt.matcher(source);
			return matcher.matches();
		} catch (Exception e) {

		}
		return false;
	}
	
	public static String isNull(Object str) {
		if (str == null) {
			return "";
		}
		
		return str.toString();
	}

	/**
	 * 
	 * @param host
	 *            ?????????? ?????? http?? o?? ????? / ?? ????? ??
	 * @param port
	 *            0???? ??? ??? ???
	 * @param method
	 *            / ???? ?????????
	 * @param param
	 * @return
	 */
	public static String buildUrl(String host, int port, String method,
			ArrayList<SimpleNameValuePair> param) {
		StringBuffer sb = new StringBuffer();
		if (host == null || host.trim().length() == 0) {
			return null;
		}
		if (!isUrl(host)) {
			sb.append("http://");
		}
		sb.append(host);

		if (port > 0) {
			sb.append(":" + port);
		}
		sb.append(method);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(param.get(i).getName());
				sb.append("=");
				sb.append(param.get(i).getValue());
			}
		}
		return sb.toString();
	}
	public static String buildUrl(String host, int port, String method,
			String param) {
		StringBuffer sb = new StringBuffer();
		if (host == null || host.trim().length() == 0) {
			return null;
		}
		if (!isUrl(host)) {
			sb.append("http://");
		}
		sb.append(host);

		if (port > 0) {
			sb.append(":" + port);
		}
		sb.append(method);
		if(!isTextEmpty(param)){
			sb.append("?"+param);
		}
		return sb.toString();
	}

	public static String milisecondToDateString(long milisecond, int style, String timeZoneStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milisecond);
		DateFormat df = DateFormat.getDateInstance(style, LocaleContextHolder.getLocale());
		
		if(!StringUtils.isEmpty(timeZoneStr)){
			df.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		}
		String outputDate = df.format(calendar.getTime());
		
		return outputDate ;
	}

	public static String milisecondToDateTimeString(long milisecond, String timeZoneStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milisecond);
		DateFormat df = DateFormat.getDateTimeInstance(2, 3, LocaleContextHolder.getLocale());
		
		String outputDate = "";
		
		if(StringUtils.isEmpty(timeZoneStr)){
			outputDate = df.format(calendar.getTime()) + " UTC/GMT" ;
		}else{
			df.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
			outputDate = df.format(calendar.getTime());
		}
		
		return outputDate ;
	}
	
	public static String milisecondToMonthDayString(long milisecond, Locale locale) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milisecond);
		SimpleDateFormat df = new SimpleDateFormat("MMM d", locale);
		
		String outputDate = "";
		
		if(StringUtils.isEmpty(locale.toString())){
			outputDate = df.format(calendar.getTime()) + " UTC/GMT" ;
		}else{
			df.setTimeZone(TimeZone.getTimeZone(locale.toString()));
			outputDate = df.format(calendar.getTime());
		}
		
		if(locale.toString().equals("ko_KR")||locale.toString().equals("ko")){
			outputDate+="일";
		}
		
		return outputDate;
	}

	public static String dateToDateTimeString(Date date, String timeZoneStr, Locale locale) {
		DateFormat df = DateFormat.getDateTimeInstance(2, 3, locale);
		
		String outputDate = "";
		
		if(StringUtils.isEmpty(timeZoneStr)){
			outputDate = df.format(date) + " UTC/GMT" ;
		}else{
			df.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
			outputDate = df.format(date);
		}
		
		return outputDate ;
	}	
	
	public static String milisecondToDateTimeString(long milisecond, int dateSyle, int timeStyle, String timeZoneStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milisecond);
		DateFormat df = DateFormat.getDateTimeInstance(dateSyle, timeStyle, LocaleContextHolder.getLocale());
		
		String outputDate = "";
		
		if(StringUtils.isEmpty(timeZoneStr)){
			outputDate = df.format(calendar.getTime()) + " UTC/GMT" ;
		}else{
			df.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
			outputDate = df.format(calendar.getTime());
		}
		
		return outputDate ;
	}	
	
	public static String getLocalFormatDate(String date, int style) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate;
		String outputDate = "";
		try {
			inputDate = format.parse(date);
			DateFormat df = DateFormat.getDateInstance(style, LocaleContextHolder.getLocale());
			outputDate = df.format(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outputDate;
	}

	public static String getLocalFormatDateTime(String dateTime, int dateStyle, int timeStyle, Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date inputDate;
		String outputDate = "";
		try {
			inputDate = format.parse(dateTime);
			DateFormat df = DateFormat.getDateInstance(dateStyle, locale);
			DateFormat tf = DateFormat.getTimeInstance(timeStyle, locale);
			outputDate = df.format(inputDate) + " " + tf.format(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outputDate + " UTC/GMT";
	}	
	
	public static String addNumberFormatWithCurrency(String currency, double number) {
		if (currency != null && currency.equalsIgnoreCase("USD")) {
			return "USD " +addNumberFormat(currency, number); 
		} 
		return addNumberFormat(currency, number)+" KRW";
		
	}
	public static String addNumberFormat(String currency, double number) {
		if (currency != null && currency.equalsIgnoreCase("USD")) {
			return usdFormat.format(number);
		}
		return krwFormat.format(number);
	}

	public static String rateSentence(String srcCurrency, double srcMoney, String dstCurrency, double dstMoney, double exchangeRate){
		StringBuffer sb = new StringBuffer();
		
		if (exchangeRate < 1) {
			sb.append(addNumberFormat(srcCurrency,1 / exchangeRate) + " " + srcCurrency + " = "
					+ " 1 " + dstCurrency);
		} else {
			sb.append("1 " + srcCurrency + " = " + addNumberFormat(dstCurrency,1 * exchangeRate)
					+ " " + dstCurrency);
		}
		return sb.toString();
	}

	public static int itemSaved(String srcCurrency, double savedMoney, double exchangeRate, String item){
		double num = 0;
		double itemValue = 0.0;
		if (StringUtils.isNotEmpty(item)){
			if (item.equalsIgnoreCase("coffee")) { itemValue = coffeeValue; }
			else if (item.equalsIgnoreCase("beer")) { itemValue = beerValue; }
			else itemValue = burgerValue;
		}else{
			itemValue = coffeeValue;
		}
		if (exchangeRate < 1) {	// srcCurrency == KRW
			num = (savedMoney * exchangeRate) / itemValue;
		} else {
			num = savedMoney / itemValue;
		}
		return (int) Math.round(num);
	}
	
	public static String getTimezone(HttpServletRequest request, UserInformation user){
		
		String country = StringUtils.defaultIfEmpty(request.getHeader("CloudFront-Viewer-Country"), "");
		String ip = StringUtils.defaultIfEmpty(request.getHeader("X-Forwarded-For"), "").split(",")[0];
		String timezone = "";
		try{
			if (!country.equalsIgnoreCase("KR")){
				if( (null == user || null == user.getTimezone() || user.getTimezone().isEmpty()) && StringUtils.isNotEmpty(ip)){
	    			URL url = new URL("http://freegeoip.net/json/" + ip);
	    			InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
	    			
	    			JSONObject object = (JSONObject)JSONValue.parseWithException(isr);
	    			String tmpTimezone = (String)(object.get("time_zone"));
	    			if(!tmpTimezone.isEmpty()){
	    				timezone = tmpTimezone;
	    			}
				}
			}else{
				timezone = "Asia/Seoul";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return timezone;
	}
	
	public static String getDateFormat(int style) {
		DateFormat format = DateFormat.getDateInstance(style, LocaleContextHolder.getLocale());
		SimpleDateFormat sdf = (SimpleDateFormat) format;
		String result = sdf.toLocalizedPattern();
		return result;
	}	
}
