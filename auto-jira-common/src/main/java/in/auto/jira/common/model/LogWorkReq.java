package in.auto.jira.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 记录工时请求模型
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-07 15:06:58
 */
@Getter
@Setter
@Builder
public class LogWorkReq {
    // 不填参数
    private Object attributes;
    private String billableSeconds;
    private Object comment;
    private Object remainingEstimate;
    private Object endDate;
    // 必填参数
    private Boolean includeNonWorkingDays;
    private String worker;
    private String started;
    private String originTaskId;
    private Long timeSpentSeconds;
}
