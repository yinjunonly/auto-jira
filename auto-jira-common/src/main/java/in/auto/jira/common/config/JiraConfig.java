package in.auto.jira.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * jira基础配置
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:34:02
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jira")
public class JiraConfig {
    private String domain;
    private Api apis;

    @Getter
    @Setter
    public static class Api {
        private String login;
        private String getIssueParams;
        private String createIssue;
        private String logWork;
    }
}
