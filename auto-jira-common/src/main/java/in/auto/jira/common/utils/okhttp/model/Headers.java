package in.auto.jira.common.utils.okhttp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * http header 参数类（hashMap包装类）
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月13日 下午12:00:40
 * @param <K> header 键
 * @param <V> header 值
 */
public class Headers<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取一个headers新实例
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月13日 下午12:08:32
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Headers<K, V> newInstance() {
		return new Headers<K, V>();
	}

	/**
	 * 通过map获取一个header对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月13日 下午12:06:49
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> Headers<K, V> of(Map<K, V> map) {
		Headers<K, V> result = new Headers<K, V>();
		for (Entry<K, V> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
