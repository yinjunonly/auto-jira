package in.auto.jira.rest.error;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.Maps;

/**
 * 自定义的错误参数处理类
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年9月3日上午10:14:13
 *
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> map = Maps.newHashMap();
		Throwable error = getError(webRequest);
		map.put("throwable", error);
		return map;
	}
}
