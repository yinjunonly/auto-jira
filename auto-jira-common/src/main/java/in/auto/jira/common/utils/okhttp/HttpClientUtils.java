package in.auto.jira.common.utils.okhttp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import in.auto.jira.common.utils.ReflectionUtils;
import in.auto.jira.common.utils.Tools;
import in.auto.jira.common.utils.okhttp.error.OkHttpErrorcode;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

/**
 * HTTP客户端工具
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月9日 下午3:12:03
 */
@Slf4j
public class HttpClientUtils {
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

	/**
	 * 对象转换为MAP映射表
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:12:17
	 * @param object java对象
	 * @return
	 */
	public static Map<String, Object> obj2Map(Object object) {
		Field[] fields = ReflectionUtils.getAllDeclaredFields(object.getClass());
		Map<String, Object> paramMaps = Maps.newHashMap();
		for (Field field : fields) {
			String fieldName = field.getName();
			ReflectionUtils.makeAccessible(field);
			Object value = ReflectionUtils.getFieldVal(field, object);
			if (value != null) {
				paramMaps.put(fieldName, value);
			}
		}
		return paramMaps;
	}

	/**
	 * 处理返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:51:59
	 * @param response
	 * @return
	 */
	public static String handleResp(Response response) {
		try {
			if (response.isSuccessful()) {
				try {
					return response.body().string();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					throw ErrorCodeExceptionFactory.build(OkHttpErrorcode.RESP_IO_ERROR);
				}
			} else {
				throw ErrorCodeExceptionFactory.build(
						Tools.fillPlaceholder(OkHttpErrorcode.RESP_STATUS_ERROR.getMessage(), response.code()),
						response.code());
			}
		} finally {
			response.close();
		}
	}
}
