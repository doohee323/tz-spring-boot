package com.tz.basis.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * </pre>
 * 
 * @version 1.0
 */
public class HttpUtil {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static final String Boundary = "--7d021a37605f0";

	/**
	 * <pre>
	 * </pre>
	 */
	public static String callUrl(String urlStr, Map input, Map header) throws Exception {
		try {
			return callUrl(urlStr, input, new ArrayList<Map<String, Object>>(), header);
		} catch (Exception e) {
			logger.debug(e.toString());
			throw new Exception(e);
		}
	}

	/**
	 * <pre>
	 * URL을 호출 하는 메소드
	 * </pre>
	 * 
	 * @param urlStr
	 * @param input
	 * @param mData
	 * @param header
	 * @return htmls
	 * @throws Exception
	 */
	public static String callUrl(String urlStr, Map input, List<Map<String, Object>> mData, Map header) throws Exception {
		String htmls = new String();
		try {
			String charset = StringUtil.getText(header.get("charset")); // euc-kr, utf-8 ..., noConvert

			String strParam = "";
			if (mData != null && mData.size() > 0) {
				for (int i = 0; i < mData.size(); i++) {
					strParam += urlEncode((HashMap) mData.get(i), charset) + "&";
				}
				if (strParam.length() > 0)
					strParam = strParam.substring(0, strParam.length() - 1);
			} else {
				strParam = urlEncode(input, charset);
			}

			if (charset.equals("noConvert"))
				charset = "";
			String inboundCharset = StringUtil.getText(header.get("inboundCharset"));
			if (!inboundCharset.equals(""))
				charset = inboundCharset;

			htmls = callUrl(urlStr, strParam, header, charset);
		} catch (Exception e) {
			logger.debug("callUrl : " + e.toString());
			System.out.print("callUrl : " + e.toString());
			throw new Exception(e);
		}
		return htmls;
	}

	/**
	 * <pre>
	 * URL을 호출 하는 메소드
	 * </pre>
	 * 
	 * @param urlStr
	 * @param strParam
	 * @param header
	 * @param charset
	 * @return htmls
	 * @throws Exception
	 */
	public static String callUrl(String urlStr, String strParam, Map<String, Object> header, String charset)
			throws Exception {
		StringBuffer htmls = new StringBuffer();
		URL url = new URL(urlStr);
		System.out.print("callUrl : " + url.toString());
		URLConnection httpConn = null;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			httpConn = url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setUseCaches(false);
			if (header != null) {
				Set<String> dataKeySet = header.keySet();
				Iterator<String> dataIterator = dataKeySet.iterator();
				while (dataIterator.hasNext()) {
					String dataKey = dataIterator.next().toString();
					if (!dataKey.equals("charset") && !dataKey.equals("inboundCharset")) {
						httpConn.setRequestProperty(dataKey, header.get(dataKey).toString());
						logger.debug("====setRequestProperty : " + dataKey + " => " + header.get(dataKey).toString());
					}
				}
			}
			//out = new PrintWriter( httpConn.getOutputStream() );
			out = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), "utf-8"));
			out.print(strParam);
			logger.debug("====urlStr:" + urlStr);
			//logger.debug("====strParam:" + strParam);
			out.flush();

			InputStream is = httpConn.getInputStream();

			if (charset != null && !charset.equals(""))
				in = new BufferedReader(new InputStreamReader(is, charset), 8 * 1024);
			else
				in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

			String line = null;
			while ((line = in.readLine()) != null) {
				htmls.append(line);
				htmls.append("\r\n");
			}
		} catch (Exception e) {
			logger.debug(e.toString());
			throw new Exception(e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
					logger.debug(e.toString());
					throw new Exception(e);
				}
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					logger.debug(e.toString());
					throw new Exception(e);
				}
		}
		return htmls.toString();
	}

	/**
	 * <pre>
	 * url encode 를 하는 메소드
	 * </pre>
	 * 
	 * @param hash
	 * @param charset
	 * @return buf.toString()
	 */
	@SuppressWarnings("deprecation")
	public static String urlEncode(Map<String, Object> hash, String charset) {
		if (hash == null)
			throw new IllegalArgumentException("argument is null");
		boolean isFirst = true;
		StringBuffer buf = new StringBuffer();

		Set<String> dataKeySet = hash.keySet();
		Iterator<String> dataIterator = dataKeySet.iterator();
		while (dataIterator.hasNext()) {
			if (isFirst)
				isFirst = false;
			else
				buf.append('&');

			try {
				String dataKey = dataIterator.next().toString();
				String value = StringUtil.getText(hash.get(dataKey));

				if (charset != null && !charset.equals("") && !charset.equals("noConvert")) {
					buf.append(URLEncoder.encode(dataKey, charset));
				} else if (charset.equals("noConvert")) {
					buf.append(dataKey);
				} else {
					buf.append(URLEncoder.encode(dataKey));
				}
				buf.append('=');
				if (charset != null && !charset.equals("") && !charset.equals("noConvert")) {
					buf.append(URLEncoder.encode(value, charset));
				} else if (charset.equals("noConvert")) {
					buf.append(value);
				} else {
					buf.append(URLEncoder.encode(value));
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
			}
		}
		return buf.toString();
	}

	/**
	 * <pre>
	 * URL Decode를 하는 메소드
	 * </pre>
	 * 
	 * @param buf
	 * @param charset
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String urlDecode(String buf, String charset) {
		try {
			if (charset != null && !charset.equals(""))
				return URLDecoder.decode(buf, charset);
			else
				return URLDecoder.decode(buf);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	/**
	 * <pre>
	 * URL Decode를 하는 메소드
	 * </pre>
	 * 
	 * @param data
	 * @param charset
	 * @return data
	 */
	public static Map<String, Object> urlDecode(Map<String, Object> data, String charset) {
		try {
			Set<String> dataKeySet = data.keySet();
			Iterator<String> dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				if (data.get(dataKey).getClass().toString().indexOf("java.lang.String") > -1) {
					try {
						data.put(dataKey, URLDecoder.decode(data.get(dataKey).toString(), charset));
					} catch (Exception e) {
						logger.debug(e.toString());
					}
				}
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		return data;
	}

	/**
	 * <pre>
	 * url encode 시 characte set을 설정하는 메소드
	 * </pre>
	 * 
	 * @param val
	 * @param encode
	 * @return val
	 */
	public static String encodeCharset(String val, String encode) {
		if (val != null && !encode.equals("")) {
			try {
				val = new String(URLEncoder.encode(val, encode));

			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
			}
		}
		return val;
	}

	/**
	 * <pre>
	 * url decode 시 characte set을 설정하는 메소드
	 * </pre>
	 * 
	 * @param val
	 * @param encode
	 * @return val
	 */
	@SuppressWarnings("deprecation")
	public static String decodeCharset(String val, String encode) {
		if (val != null && !encode.equals("")) {
			try {
				val = new String(URLDecoder.decode(val).getBytes(), encode);
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
			}
		}
		return val;
	}

	/**
	 * <pre>
	 * post로 파일 업로드후 리턴값 받기
	 * </pre>
	 * 
	 * @param url
	 * @param fileList
	 * @param fileInfo
	 * @throws Exception
	 */
	public static void uploadFile(URL url, List<File> fileList, List<String> fileInfo) throws Exception {
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setChunkedStreamingMode(1024);

		httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);
		DataOutputStream httpOut = new DataOutputStream(httpConn.getOutputStream());

		for (int i = 0; i < fileList.size(); i++) {
			File f = fileList.get(i);
			String str = "--" + Boundary + "\r\n" + "Content-Disposition: form-data;name=\"" + fileInfo.get(i)
					+ "\"; filename=\"" + f.getName() + "\"\r\n" + "Content-Type: image/png\r\n" + "\r\n";

			httpOut.write(str.getBytes());

			FileInputStream uploadFileReader = new FileInputStream(f);
			int numBytesToRead = 1024;
			int availableBytesToRead;
			while ((availableBytesToRead = uploadFileReader.available()) > 0) {
				byte[] bufferBytesRead;
				bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead]
						: new byte[availableBytesToRead];
				uploadFileReader.read(bufferBytesRead);
				httpOut.write(bufferBytesRead);
				httpOut.flush();
			}
			httpOut.write(("--" + Boundary + "--\r\n").getBytes());
		}

		httpOut.write(("--" + Boundary + "--\r\n").getBytes());

		httpOut.flush();
		httpOut.close();

		// read & parse the response
		InputStream is = httpConn.getInputStream();
		StringBuilder response = new StringBuilder();
		byte[] respBuffer = new byte[4096];
		while (is.read(respBuffer) >= 0) {
			response.append(new String(respBuffer).trim());
		}
		is.close();
		logger.debug(response.toString());
	}

	/**
	 * <pre>
	 * url로부터 파일을 생성 (이어받기)
	 * </pre>
	 * 
	 * @param urlStr
	 * @param fileNm
	 * @throws Exception
	 */
	public static void makeFileFromUrl(String urlStr, String fileNm) throws Exception {
		int nDone = 0;
		int nTimeout = 30000;
		long fileSize, remains, lenghtOfFile = 0;

		RandomAccessFile output = null;
		InputStream input = null;
		try {
			File file = new File(fileNm);
			if (file.exists() == false) {
				file.createNewFile();
			}
			output = new RandomAccessFile(file.getAbsolutePath(), "rw");

			fileSize = output.length();
			output.seek(fileSize);

			URL url = new URL(urlStr);
			URLConnection httpConn = url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-');
			httpConn.connect();
			httpConn.setConnectTimeout(nTimeout);
			httpConn.setReadTimeout(nTimeout);
			remains = httpConn.getContentLength();
			lenghtOfFile = remains + fileSize;

			if ((remains <= nDone) || (remains == fileSize)) {
				return;
			}
			input = httpConn.getInputStream();

			byte data[] = new byte[1024];
			int count = 0;

			if (fileSize < lenghtOfFile) {
				while ((count = input.read(data)) != -1) {
					output.write(data, 0, count);
				}
			}
		} catch (Exception e) {
			logger.debug("error!!!" + e.toString());
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (java.io.IOException e1) {
				logger.debug("error!!!" + e1.toString());
			}
		}
	}

	/**
	 * <pre>
	 * content-type이 "multipart/form-data"인지를 검사한다.
	 * request.getContentType() method를 사용하며 이때 구해지는 문자열을 적절한 크기로 잘라서 비교하는 logic을 사용한다.
	 * (참고) html의 form에서 특별하게 enctype을 지정하지 않는경우 content-Type은 application/x-www-form-urlencoded이다.
	 * </pre>
	 * 
	 * @param req
	 *          servlet에서 전달받은 HttpServletRequest. 실제의 content type을 구하기 위해 사용한다.
	 * @return boolean "multipart/form-data"인경우 true, 그렇지 않을경우 false.
	 */
	public static boolean isMultipart(HttpServletRequest req) {
		String contentType = null;
		String multipartContentType = "multipart/form-data";
		contentType = req.getContentType();

		return (contentType != null && contentType.length() > 19
				&& multipartContentType.equals(contentType.substring(0, 19))) ? true : false;
	}

	public static void infoHeader(HttpURLConnection con) {
		System.out.println("############################################################");
		System.out.println("con : " + con.getURL());

		Map<String, List<String>> m = con.getHeaderFields();
		for (Iterator<String> i = m.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			System.out.println("key : " + key + ", value : " + m.get(key));
		}
	}

	public static String getCookies(HttpURLConnection con) {
		String cookies = "";

		Map<String, List<String>> m = con.getHeaderFields();
		if (m.containsKey("Set-Cookie")) {
			Collection<String> c = (Collection<String>) m.get("Set-Cookie");
			for (Iterator<String> i = c.iterator(); i.hasNext();) {
				cookies += (String) i.next() + ", ";
			}
		}
		return cookies;
	}

	@SuppressWarnings("unchecked")
	public static void setCookies(HttpURLConnection con, String cookies) {
		@SuppressWarnings("rawtypes")
		Map m = con.getHeaderFields();
		m.put("Set-Cookie", cookies);
	}

	/**
	 * <pre>
	 * main(String[] args)
	 * </pre>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	}

	public static Map<String, Object> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, Object> map = new HashMap<String, Object>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = "";
			if (param.split("=").length > 1)
				value = param.split("=")[1];

			map.put(name, value);
		}
		return map;
	}

}
