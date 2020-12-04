package in.auto.jira.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import in.auto.jira.common.exceptions.ErrorCodeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 值转换工具
 * 
 * @author yinju
 * @date 2018年6月5日下午4:24:09
 */
@Slf4j
public class ValueConvertUtils {
	/**
	 * 将String类型的数据转换为指定类型
	 * 
	 * @author yinju
	 * @param clazz     指定的类型
	 * @param type      指定数据的真实类型（泛型时出现）
	 * @param stringVal String类型的值
	 * @return
	 */
	public static Object string2Object(Class<?> clazz, Type type, String stringVal) {
		if (stringVal != null) {
			if (clazz == Integer.class || clazz == int.class) {
				return Integer.parseInt(stringVal);
			} else if (clazz == Double.class || clazz == double.class) {
				return Double.parseDouble(stringVal);
			} else if (clazz == Float.class || clazz == float.class) {
				return Float.parseFloat(stringVal);
			} else if (clazz == Boolean.class || clazz == boolean.class) {
				return Boolean.parseBoolean(stringVal);
			} else if (clazz == Long.class || clazz == long.class) {
				return Long.parseLong(stringVal);
			} else if (clazz == Date.class) {
				return TypeUtils.castToDate(stringVal);
			} else if (clazz == BigDecimal.class) {
				return TypeUtils.castToBigDecimal(stringVal);
			} else if (clazz == String.class) {
				return stringVal;
			} else if (Enum.class.isAssignableFrom(clazz)) {
				return TypeUtils.castToEnum(stringVal.toUpperCase(), clazz, null);
			} else if (Collection.class.isAssignableFrom(clazz)) {
				ParameterizedType pt = (ParameterizedType) type;
				try {
					return JSON.parseArray(stringVal, Class.forName(pt.getActualTypeArguments()[0].getTypeName()));
				} catch (ClassNotFoundException e) {
					log.error("将值String类型的值转成其他类型，报错：{}", e.getMessage());
					throw new ErrorCodeException(in.auto.jira.common.enums.SystemErrorCode.STRING_TO_VALUE_ERROR);
				}
			} else {
				return JSON.parseObject(stringVal, clazz);
			}
		}
		return stringVal;
	}

	/**
	 * 将对象转成String
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param val
	 * @return
	 */
	public static String object2String(Object val) {
		if (BeanUtils.isSimpleProperty(val.getClass())) {
			return val.toString();
		} else {
			return JSON.toJSONString(val);
		}
	}
}