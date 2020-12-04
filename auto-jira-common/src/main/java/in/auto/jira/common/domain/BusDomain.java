package in.auto.jira.common.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 自动化总线模型
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:22:33
 */
@Getter
@Setter
public class BusDomain {
    private String loginName;
    private String password;
    private List<LogWorkDomain> works;
}
