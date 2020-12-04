package in.auto.jira.common.utils.okhttp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * okhttp配置信息
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月12日 上午11:46:02
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "okhttp")
public class OkHttpProperties {
	/** 是否重试失败连接，默认为false */
	private Boolean retryOnConnectionFailure = false;
	/** 连接超时时间，单位秒，默认10秒 */
	private Long connectTimeout = 10L;
	/** 读取超时时间，单位秒，默认10秒 */
	private Long readTimeout = 10L;
}
