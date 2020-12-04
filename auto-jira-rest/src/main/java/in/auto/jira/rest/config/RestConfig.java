package in.auto.jira.rest.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * rest配置
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年12月13日下午3:51:15
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest")
public class RestConfig {
	/** 是否在运行时混入前端，默认不混入 */
	private boolean mixinFront = false;
	/** 前端是否为SPA */
	private boolean spa = true;
	/** 前端应用列表 */
	private List<String> frontApps = Lists.newArrayList();
	/** 前端默认首页 */
	private String defaultPage = "index.html";
	/** 静态资源的路径排除 */
	private String[] staticResourcePathPatterns = { "/", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png",
			"/**/*.gif", "/**/*.ico", "/**/*.ttf", "/**/*.woff", "/**/*.eot", "/**/*.svg", "/**/*.jpg", "/**/*.txt",
			"/**/*.json", "/**/*.js.map", "/**/*.css.map" };
}
