package in.auto.jira.common.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 参数排序成URL的工具类
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年6月20日 上午11:09:57
 */
public class ParamSortToUrlUtils {
	/** 常量，等于符号 */
	private static final String EQ = "=";
	/** 常量，并且符号 */
	private static final String AND = "&";

	/**
	 * 将对象转换为URL参数拼接的样子，并且使用Ascii排序
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月17日 下午2:54:58
	 * @param object           java对象
	 * @param ignoreFieldNames 忽略的属性名
	 * @return
	 */
	public static String toURLForAsciiSort(Object object, String... ignoreFieldNames) {
		// 取得所有参数
		Field[] fields = ReflectionUtils.getAllDeclaredFields(object.getClass());
		Map<String, Object> paramMaps = Maps.newHashMap();
		for (Field field : fields) {
			String fieldName = field.getName();
			ReflectionUtils.makeAccessible(field);
			paramMaps.put(fieldName, ReflectionUtils.getFieldVal(field, object));
		}
		// 调用map方法
		return toURLForAsciiSort(paramMaps, ignoreFieldNames);
	}

	/**
	 * 将对象转换为URL参数拼接的样子，并且使用Ascii排序
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月17日 下午2:54:58
	 * @param paramMaps        参数映射表
	 * @param ignoreFieldNames 忽略的属性名
	 * @return
	 */
	public static String toURLForAsciiSort(Map<String, Object> paramMaps, String... ignoreFieldNames) {
		// 1、参数排序
		Set<String> params = paramMaps.keySet();
		List<String> sortParams = Lists.newArrayList(params);
		Collections.sort(sortParams);
		// 2、URL拼接
		return createURL(sortParams, paramMaps, ignoreFieldNames);
	}

	/**
	 * 根据参数名称顺序将Map参数列表转换为URL
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月17日 下午3:29:15
	 * @param sortParams       排序属性
	 * @param paramMaps        参数映射表
	 * @param ignoreFieldNames 忽略的属性列名
	 * @return
	 */
	public static final String createURL(List<String> sortParams, Map<String, Object> paramMaps,
			String... ignoreFieldNames) {
		StringBuilder paramsURL = new StringBuilder();
		Set<String> ignoreParamsSet = Sets.newConcurrentHashSet();
		for (String fieldName : ignoreFieldNames) {
			ignoreParamsSet.add(fieldName);
		}
		for (int i = 0; i < sortParams.size(); i++) {
			String item = sortParams.get(i);
			Object value = paramMaps.get(item);
			// 不存在在排除列表中并且不为null时
			if (!Tools.isNull(value) && !ignoreParamsSet.contains(item)) {
				paramsURL.append(item).append(EQ).append(ValueConvertUtils.object2String(value)).append(AND);
			}
		}
		// 去除最后一位&符号
		if (paramsURL.length() > 0) {
			return paramsURL.substring(0, paramsURL.length() - 1);
		}
		return paramsURL.toString();
	}

	/**
	 * 根据参数名称顺序将对象转换为URL
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年6月20日 下午12:38:52
	 * @param sortParams       排序属性
	 * @param object           对象
	 * @param ignoreFieldNames 忽略的属性列名
	 * @return
	 */
	public static final String createURL(List<String> sortParams, Object object, String... ignoreFieldNames) {
		// 取得所有参数
		Field[] fields = ReflectionUtils.getAllDeclaredFields(object.getClass());
		Map<String, Object> paramMaps = Maps.newHashMap();
		for (Field field : fields) {
			String fieldName = field.getName();
			ReflectionUtils.makeAccessible(field);
			paramMaps.put(fieldName, ReflectionUtils.getFieldVal(field, object));
		}
		return createURL(sortParams, paramMaps, ignoreFieldNames);
	}
}
