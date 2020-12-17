package in.auto.jira.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 记录工时模型
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:14:19
 */
@Getter
@Setter
public class LogWorkDomain {
    private String projectId;
    private String issueTypeId;
    private String atlToken;
    private String formToken;
    private String summary;
    private String categoryId;
    private String subCategoryId;
    private String assignee;
    private String logDate;
    private Integer hours;
    private String issueId;
    private String categoryType;
}
