package in.auto.jira.common.utils.okhttp.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * okhttp连接池配置
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月12日 下午2:24:17
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "okhttp.connection-pool")
public class ConnectionPoolProperties {
	/** 连接池最大连接数，默认200 */
	private Integer maxIdleConnections = 200;
	/** 保持活动时间，默认60s */
	private Long keepAliveDuration = 60L;
	/** 保持活动时间单位，默认为秒 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;
}
