package in.auto.jira.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具箱
 * 
 * @author yinjun
 * @date 2015年9月6日 16:09:06
 */
public class Tools {

	public static Long formmatLong(String s) {
		if(null == s || s.isEmpty()) {
			return null;
		}
		if(s.contains(".")) {
			s = s.substring(0, s.indexOf("."));
		}
		long l;
		try {
			l = Long.parseLong(s.replace(",","").replace("，",""));
		} catch (Exception e) {
			l = 0L;
		}
		return l;
	}

	public static Double formmatDouble(String s) {
		if (null == s || s.isEmpty()) {
			return null;
		}
		double d;
		try {
			d = Double.parseDouble(s);
		} catch (Exception e) {
			d = 0D;
		}
		return d;
	}

	/**
	 * 判断集合是否为NUll或空集合
	 * 
	 * @author yinjun
	 * @param collection 集合
	 * @return 返回布尔值
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> collection) {
		return collection == null || collection.size() <= 0;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @author yinjun
	 * @param strs 字符串数组
	 * @return 返回布尔值
	 */
	public static boolean isNullOrEmpty(String... strs) {
		if (strs != null) {
			for (int i = 0; i < strs.length; i++) {
				if (strs[i] == null || strs[i].trim().length() <= 0) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * 判断map是否为NUll或空集合
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
		return map == null || map.size() <= 0;
	}

	/**
	 * 判断数字型参数是否等于null或者零
	 * 
	 * @author yinjun
	 * @param number 数字集合
	 * @return 返回布尔值
	 */
	public static Boolean isNullOrZero(Number... number) {
		if (number != null) {
			for (Number num : number) {
				if (num == null || num.doubleValue() == 0) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @author yinjun
	 * @param obj 对象数组
	 * @return 返回布尔值
	 */
	public static Boolean isNull(Object... obj) {
		if (obj != null) {
			for (Object object : obj) {
				if (object == null) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 获取32位随机字符
	 * 
	 * @author yinjun
	 * @return 32位随机字符
	 */
	public static String getRandom() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 获取首选值(当目标值为空时，返回默认值)
	 * 
	 * @author yinjun
	 * @param targetVal  目标值
	 * @param defaultVal 默认值
	 * @return 首选值
	 */
	public static <T> T getPreferredVal(T targetVal, T defaultVal) {
		return targetVal != null ? targetVal : defaultVal;
	}

	/**
	 * 随机数对象
	 */
	private final static Random RAND = new Random();

	/***
	 * 获取有上限的随机整数
	 * 
	 * @author yinjun
	 * @param maxVal 上限值
	 * @return
	 */
	public static Integer getRandom(Integer maxVal) {
		// 从1到最大值
		int randNum = RAND.nextInt(maxVal) + 1;
		return randNum;
	}

	/**
	 * 元转分
	 * 
	 * @author yinjun
	 * @param yuan
	 * @return
	 */
	public static Integer yuanConvertfen(Double yuan) {
		if (yuan != null) {
			return (int) (yuan * 100);
		}
		return null;
	}

	/**
	 * 分转元
	 * 
	 * @author yinjun
	 * @param fen
	 * @return
	 */
	public static Double fenConvertyuan(Integer fen) {
		if (fen != null) {
			BigDecimal bg = new BigDecimal((double) (fen * 0.01));
			double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}
		return null;
	}

	/**
	 * 获取反绝对值
	 * 
	 * @author yinjun
	 * @param price 整数
	 * @return 负数
	 */
	public static Integer unAbs(Integer price) {
		if (price != null) {
			return (price > 0) ? -price : price;
		}
		return null;
	}

	/**
	 * 保留两位小数(四舍五入)
	 * 
	 * @author yinjun
	 * @param val 目标值
	 * @return 保留两位小数后的值
	 */
	public static Double doubleRound(Double val) {
		BigDecimal bg = new BigDecimal(val);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 为序号填充0（当序号满足位数则不填充）
	 * 
	 * @author yinjun
	 * @param seq   序号
	 * @param digit 填充后位数
	 * @return
	 */
	public static String lfill(Long seq, Integer digit) {
		if (!Tools.isNullOrZero(digit)) {
			String str = String.format("%0" + digit + "d", seq);
			return str;
		} else {
			return seq.toString();
		}
	}

	/**
	 * map转换为url
	 * 
	 * @author yinjun
	 * @param parameters
	 * @return
	 */
	public static String mapToUrl(Map<String, Object> parameters) {
		StringBuffer parametersStr = new StringBuffer();
		Set<Entry<String, Object>> entries = parameters.entrySet();
		// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String, Object>> it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			// 参数不为空或者null时拼接进去
			if (value != null && !value.equals("")) {
				parametersStr.append(key + "=" + value + "&");
			}
		}
		return parametersStr.toString();
	}

	/**
	 * 字符集合转成字符分割的字符串
	 * 
	 * @author yinjun
	 * @param list      集合
	 * @param separator 分隔符（传null则为,）
	 * @return
	 */
	public static String listToString(List<String> list, char separator) {
		separator = getPreferredVal(separator, ',');
		if (!isNullOrEmpty(list)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					sb.append(list.get(i));
				} else {
					sb.append(list.get(i));
					sb.append(separator);
				}
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * {}占位符顺序替换（和slf4j替换规则一样）
	 * 
	 * @author yinjun
	 * @param content 待替换内容
	 * @param objects 动态参数列表
	 * @return 替换后的内容
	 */
	public static String fillPlaceholder(String content, Object... objects) {
		if (!isNull(objects, content)) {
			String[] contents = content.split("\\{\\}");
			StringBuffer result = new StringBuffer(contents[0]);
			for (int i = 1; i <= objects.length; i++) {
				result.append(objects[i - 1]);
				if (i < contents.length) {
					result.append(contents[i]);
				}
			}
			return result.toString();
		}
		return content;
	}

	/**
	 * 中文匹配
	 */
	private static Pattern chinesePattern = Pattern.compile("([\u4e00-\u9fa5]+)");

	/**
	 * 将文本中的中文做urlencode
	 * 
	 * @author yinjun
	 * @param url 文本
	 * @return
	 */
	public static String getEncodeZHUrl(String url) {
		// 利用正则找到中文
		Matcher m = chinesePattern.matcher(url);
		String mv = null;
		// 循环查找
		while (m.find()) {
			mv = m.group(0);
			try {
				// 将中文执行urlencode操作后替换
				url = url.replace(mv, URLEncoder.encode(mv, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	/**
	 * 获取工程路径
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		return System.getProperty("user.dir");
	}

	/**
	 * 正则匹配字符是否存在，支持*通配符
	 * 
	 * @author yinju
	 * @param str   被匹配的字符
	 * @param regex 匹配字符（可带通配符）
	 * @return
	 */
	public static boolean matche(String str, String regex) {
		regex = regex.replace("*", ".*");
		return str.matches(regex);
	}

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 字节数组转16进制字符
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param bytes
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}
		return new String(buf);
	}

	/**
	 * 传入文本内容，返回 SHA-256 串
	 * 
	 * @param strText
	 * @return
	 */
	public static String SHA256(final String strText) {
		return SHA(strText, "SHA-256");
	}

	/**
	 * 传入文本内容，返回 SHA-512 串
	 * 
	 * @param strText
	 * @return
	 */
	public static String SHA512(final String strText) {
		return SHA(strText, "SHA-512");
	}

	/**
	 * 字符串 SHA 加密
	 * 
	 * @param strSourceText
	 * @return
	 */
	private static String SHA(final String strText, final String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				// SHA 加密开始
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}

	public static String urlEncoding(String url){
		try {
			return URLEncoder.encode(url,"utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return url;
	}
}