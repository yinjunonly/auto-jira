package in.auto.jira.common.domain;

import java.util.Date;

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
    private String projectName;
    private String typeName;
    private String categoryName;
    private String subCategoryName;
    private Date logDate;
    private Integer hours;
}
