package com.tz.basis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MapUtil
 * 설    명 :
 * 작 성 자 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
public class MapUtil {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

	/**
	 * <pre>
	 * List<Map<String, Object>>에 새로운 String type column을 추가한다.
	 * </pre>
	 *
	 * @param mData
	 * @param column
	 * @param value
	 */
	public static void fillColValue(List<Map<String, Object>> mData, String column, Object value) {
		int size = mData.size();
		for (int i = 0; i < size; i++) {
			mData.get(i).put(column, value);
		}
	}

	/**
	 * <pre>
	 * List<Map<String, Object>>의 String type의 특정 column 값을 수정한다.
	 * </pre>
	 *
	 * @param mData
	 * @param column
	 * @param value
	 */
	public static void modiColValue(List<Map<String, Object>> mData, String column, String value) {
		int size = mData.size();
		for (int i = 0; i < size; i++) {
			mData.get(i).put(column, value);
		}
	}

	/**
	 * <pre>
	 * List<Map<String, Object>>의 header 정보를 변경
	 * </pre>
	 *
	 * @param mData
	 * @param inputData
	 * @return newData
	 */
	public static List<Map<String, Object>> changeLMultiHeader(List<Map<String, Object>> mData, Map inputData) {
		List<Map<String, Object>> newData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mData.size(); i++) {
			Map input = (Map) mData.get(i);
			newData.add(mData.get(i));
			Set dataKeySet2 = inputData.keySet();
			Iterator dataIterator2 = dataKeySet2.iterator();
			for (Iterator iterator = inputData.keySet().iterator(); iterator.hasNext();) {
				Object code = iterator.next();
				String newCode = dataIterator2.next().toString();
				newData.get(i).put(newCode, input.get(code));
			}
		}
		return newData;
	}

	/**
	 * <pre>
	 * List<Map<String, Object>>의 header 정보를 변경
	 * </pre>
	 *
	 * @param mData
	 * @param v_header
	 * @return changeLMultiHeader
	 */
	public static List<Map<String, Object>> changeLMultiHeader(List<Map<String, Object>> mData, String v_header) {
		Map inputData = new HashMap();
		if (v_header.indexOf(",") < 0)
			return mData;
		String arryHeader[] = v_header.split(",");
		for (int i = 0; i < arryHeader.length; i++) {
			String col = arryHeader[i];
			String colNm = col.substring(0, col.indexOf(":")).trim();
			String colType = "";
			if (col.indexOf("(") > -1) {
				colType = col.substring(col.indexOf(":") + 1, col.indexOf("(")).trim();
			} else {
				colType = col.substring(col.indexOf(":") + 1, col.length()).trim();
			}
			if (colType.equals("STRING")) {
				inputData.put(colNm, "");
			} else if (colType.equals("INT")) {
				inputData.put(colNm, 0);
			} else if (colType.equals("DECIMAL")) {
				inputData.put(colNm, 0.0);
			}
		}
		return changeLMultiHeader(mData, inputData);
	}

	/**
	 * <pre>
	 * List mapToList
	 * </pre>
	 *
	 * @param list
	 * @param mData
	 * @return list
	 */
	public static List mapToList(List list, List<Map<String, Object>> mData) {
		try {
			if (list == null)
				list = new ArrayList();
			String header = "";
			for (int i = 0; i < mData.size(); i++) {
				String data = "";
				if (i == 0) {
					Map input = (Map) mData.get(i);
					Set dataKeySet = input.keySet();
					Iterator dataIterator = dataKeySet.iterator();
					while (dataIterator.hasNext()) {
						String dataKey = dataIterator.next().toString();
						header += dataKey + ",";
					}
					header = header.substring(0, header.length() - 1);
					list.add(header);
				}
				Map input = (Map) mData.get(i);
				Set dataKeySet = input.keySet();
				Iterator dataIterator = dataKeySet.iterator();
				while (dataIterator.hasNext()) {
					Object str = input.get(dataIterator.next().toString());
					if (str == null) {
						str = "";
					} else {
						// ,가 Array 처리 오류를 발생시키므로 임의 처리하고 데이터셋에서 다시 원복해서 사용
						str = StringUtils.replace(str.toString(), ",", "`");
					}
					data += str + ",";
				}
				data = data.substring(0, data.length() - 1);
				list.add(data);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * <pre>
	 * List toList
	 * </pre>
	 *
	 * @param list
	 * @param input
	 * @return list
	 */
	public static List toList(List list, Map input) {
		try {
			if (list == null)
				list = new ArrayList();
			String header = "";
			String data = "";
			Set dataKeySet = input.keySet();
			Iterator dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				header += dataKey + ",";
				Object str = input.get(dataKey);
				if (str == null) {
					str = "";
				} else {
					// ,가 Array 처리 오류를 발생시키므로 임의 처리하고 데이터셋에서 다시 원복해서 사용
					str = StringUtils.replace(str.toString(), ",", "`");
				}
				data += str + ",";
			}
			if (!header.equals("")) {
				header = header.substring(0, header.length() - 1);
				list.add(header);
				data = data.substring(0, data.length() - 1);
				list.add(data);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * <pre>
	 * Map<String, Object> queryStringToMap(String)
	 * </pre>
	 *
	 * @param String
	 * @return mData
	 */
	public static Map<String, Object> queryStringToMap(String queryString) {
		if (queryString == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		for (String str : queryString.split("&")) {
			String key = null, value = null;
			try {
				key = str.split("=")[0];
			} catch (Exception e) {
			}
			try {
				value = str.split("=")[1];
			} catch (Exception e) {
			}
			if (key != null && !key.trim().equals(""))
				map.put(key, value);
		}
		return map;
	}

	/**
	 * <pre>
	 * List<Map<String, Object>> toMap(List list)
	 * </pre>
	 *
	 * @param list
	 * @return mData
	 */
	public static List<Map<String, Object>> toMap(List list) {
		List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
		try {
			if (list == null)
				return mData;
			String head[] = null;
			Iterator dataIterator = list.iterator();
			while (dataIterator.hasNext()) {
				String str = dataIterator.next().toString();
				String strArry[] = StringUtils.split(str, ",");
				if (head == null) {
					head = strArry.clone();
				} else {
					Map data = new HashMap();
					for (int j = 0; j < head.length; j++) {
						if (head.length > strArry.length && j == (head.length - 1)) {
							data.put(head[j], "");
						} else {
							data.put(head[j], strArry[j]);
						}
					}
					mData.add(mData.size(), data);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return mData;
	}

	/**
	 * <pre>
	 * HashMap toHashMap(List list)
	 * </pre>
	 *
	 * @param list
	 * @return input
	 */
	public static HashMap toHashMap(List<?> list) {
		HashMap input = new HashMap();
		try {
			if (list == null)
				return input;
			String head[] = null;
			Iterator dataIterator = list.iterator();
			while (dataIterator.hasNext()) {
				String str = dataIterator.next().toString();
				String strArry[] = StringUtils.split(str, ",");
				if (head == null) {
					head = strArry.clone();
				} else {
					for (int j = 0; j < head.length; j++) {
						if (head.length > strArry.length && j == (head.length - 1)) {
							input.put(head[j], "");
						} else {
							input.put(head[j], strArry[j]);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return input;
	}

	/**
	 * <pre>
	 * get
	 * </pre>
	 *
	 * @param mData
	 * @param aCol
	 * @param i
	 * @return Map
	 */
	public static Object get(List<Map<String, Object>> mData, String aCol, int i) {
		if (mData.get(i) == null)
			return "";
		if (((Map) (mData.get(i))) == null)
			return "";
		if (((Map) (mData.get(i))).get(aCol) == null)
			return "";
		return ((Map) (mData.get(i))).get(aCol);
	}

	/**
	 * <pre>
	 * getDouble
	 * </pre>
	 *
	 * @param mData
	 * @param aCol
	 * @param i
	 * @return Double
	 */
	public static double getDouble(List<Map<String, Object>> mData, String aCol, int i) {
		Object obj = get(mData, aCol, i);
		return Double.parseDouble(obj.toString());
	}

	/**
	 * <pre>
	 * setNullString
	 * </pre>
	 *
	 * @param mData
	 * @return mData
	 */
	public static List<Map<String, Object>> setNullString(List<Map<String, Object>> mData) {
		for (int i = 0; i < mData.size(); i++) {
			Set dataKeySet = mData.get(i).keySet();
			Iterator dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				if (mData.get(i).get(dataKey) == null) {
					mData.get(i).put(dataKey, "");
				}
			}
		}
		return mData;
	}

	/**
	 * <pre>
	 * setNullString
	 * </pre>
	 *
	 * @param input
	 * @return input
	 */
	public static Map<String, Object> setNullString(Map<String, Object> input) {
		Set dataKeySet = input.keySet();
		Iterator dataIterator = dataKeySet.iterator();
		while (dataIterator.hasNext()) {
			String dataKey = dataIterator.next().toString();
			if (input.get(dataKey) == null) {
				input.put(dataKey, "");
			}
		}
		return input;
	}

	/**
	 * Map<String, Object> 2개를 병합하여 리턴한다.
	 * 
	 * @param header
	 *          Header 정보
	 * @return LData Raw 정
	 */
	public static Map<String, Object> mergeValue(Map<String, Object> source, Map<String, Object> target) {
		try {
			Set dataKeySet = source.keySet();
			Iterator dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				Object str = target.get(dataKey);
				if (str == null || str.toString().equals("")) {
					if (source.get(dataKey) != null && !source.get(dataKey).toString().equals("")) {
						target.put(dataKey, source.get(dataKey));
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return target;
	}

	/**
	 * Null 값이 있는 컬럼에 임의 값을 넣어 준다.
	 * 
	 * @param header
	 *          Header 정보
	 * @return LData Raw 정
	 */
	public static Map<String, Object> setDumyValue(Map<String, Object> input, String dummyStr) throws Exception {
		try {
			Set dataKeySet = input.keySet();
			Iterator dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				Object str = input.get(dataKey);
				if (str == null || str.toString().equals("")) {
					input.put(dataKey, dummyStr);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return input;
	}
}