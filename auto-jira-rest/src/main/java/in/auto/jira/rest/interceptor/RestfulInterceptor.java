package in.auto.jira.rest.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import in.auto.jira.common.utils.Tools;
import in.auto.jira.rest.config.RestConfig;
import com.google.common.base.Objects;

/**
 * rest请求拦截器
 * 
 * @author yinju
 * @date 2018年5月30日下午2:37:09
 */
@Configuration
public class RestfulInterceptor implements HandlerInterceptor {
	/** 配置文件 */
	@Resource
	private RestConfig config;

	/**
	 * 在请求进入Controller之前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1.如果为握手请求，或者混入了前端时请求accept为text_html（解决单页应用history默认刷新的问题），不拦截
		if (Objects.equal(request.getMethod(), HttpMethod.OPTIONS.name())) {
			return true;
		}
		if (config.isMixinFront()) {
			String accept = request.getHeader(HttpHeaders.ACCEPT);
			if (!Tools.isNullOrEmpty(accept)) {
				if (accept.startsWith(MediaType.TEXT_HTML_VALUE)) {
					return true;
				}
			}
		}
		return true;
	}
}
