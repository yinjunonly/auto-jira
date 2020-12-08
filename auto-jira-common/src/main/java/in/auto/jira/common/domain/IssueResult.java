package in.auto.jira.common.domain;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 问题查询返回值
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:45:48
 */
@Getter
@Setter
public class IssueResult {
    @JsonIgnore
    private List<String> errorMessages;
    @JSONField(name = "atl_token")
    private String atlToken;
    private String formToken;
    @JsonIgnore
    private List<Field> fields;
    @JsonIgnore
    private CreatedIssueDetails createdIssueDetails;

    private List<Element> projects;
    private Map<String, List<Element>> issueTypes;
    private List<Element> categorys;
    private Map<String, List<Element>> subCategorys;
    private String assignee;

    @Getter
    @Setter
    public static class Field {
        private String id;
        private String lable;
        private Boolean required;
        private String editHtml;
    }

    @Getter
    @Setter
    @Builder
    public static class Element {
        private String id;
        private String name;
    }

    @Getter
    @Setter
    public static class CreatedIssueDetails {
        private String id;
        private String key;
        private String self;
    }
}
