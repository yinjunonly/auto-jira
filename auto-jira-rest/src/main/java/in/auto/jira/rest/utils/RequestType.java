package in.auto.jira.rest.utils;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 请求方法枚举
 * 
 * @author yinju
 * @date 2018年6月4日下午2:34:48
 */
public enum RequestType {
	/**
	 * 获取资源
	 */
	GET,
	/**
	 * 新建资源
	 */
	POST,
	/**
	 * 更新资源(客户端提供所有数据)
	 */
	PUT,
	/**
	 * 更新资源（客户端只提供要更新的数据和主键）
	 */
	PATCH,
	/**
	 * 删除资源
	 */
	DELETE;

	/**
	 * 文字与枚举的映射关系
	 */
	public static final Map<String, RequestType> REQUEST_MAP = Maps.newConcurrentMap();

	/**
	 * 初始化映射关系
	 */
	static {
		for (RequestType item : RequestType.values()) {
			REQUEST_MAP.put(item.toString(), item);
		}
	}
}