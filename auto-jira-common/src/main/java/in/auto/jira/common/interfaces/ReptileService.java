package in.auto.jira.common.interfaces;

import in.auto.jira.common.domain.BusDomain;
import in.auto.jira.common.domain.IssueResult;

/**
 * 爬虫服务
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:12:07
 */
public interface ReptileService {
    /**
     * 查找快速创建参数
     * 
     * @param loginName 登录名
     * @param password  密码
     */
    IssueResult findQuickParams(String loginName, String password);

    /**
     * 保存Issue并log工时
     * 
     * @param BusDomain 总线模型
     */
    void automation(BusDomain busDomain);

    /**
     * log工时
     * 
     * @param busDomain 总线模型
     */
    void logWorkAutomation(BusDomain busDomain);
}