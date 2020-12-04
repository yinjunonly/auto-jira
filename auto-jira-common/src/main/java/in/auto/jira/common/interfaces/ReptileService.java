package in.auto.jira.common.interfaces;

import in.auto.jira.common.domain.BusDomain;

/**
 * 爬虫服务
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:12:07
 */
public interface ReptileService {
    /**
     * 记录工时
     * @param busDomain 自动化主线模型
     */
    void logWork(BusDomain busDomain);
}