package in.auto.jira.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import in.auto.jira.rest.interceptor.RestfulInterceptor;

/**
 * web配置类
 * 
 * @author yinju
 * @date 2018年5月30日下午4:09:34
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer, WebMvcRegistrations {
	/** rest请求拦截器 */
	@Autowired
	private RestfulInterceptor restfulInterceptor;
	/** rest配置 */
	@Autowired
	private RestConfig restConfig;

	/**
	 * 增加自定义拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 1.增加Restful请求拦截器，主要用来分发默认请求，用来实现系统自动化单模型CRUD操作
		// 同时排除所有的静态资源
		registry.addInterceptor(restfulInterceptor).addPathPatterns("/**").excludePathPatterns("/error")
				.excludePathPatterns(restConfig.getStaticResourcePathPatterns()).order(2);
	}

	/**
	 * 跨域处理器
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				//.allowedMethods(CorsConfiguration.ALL)
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "PATCH")
				.allowedHeaders("AUTHORIZATION", "Cache-Control", "Content-Encoding", "Content-Language",
						"Content-Length", "Content-Location", "Content-Range", "Content-Type", "Access-Control-Request-Headers", "X-Requested-With")
				.allowCredentials(true);
	}
}