package in.auto.jira.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import org.apache.commons.compress.utils.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.auto.jira.common.config.JiraConfig;
import in.auto.jira.common.domain.BusDomain;
import in.auto.jira.common.domain.IssueResult;
import in.auto.jira.common.domain.LogWorkDomain;
import in.auto.jira.common.domain.IssueResult.Field;
import in.auto.jira.common.exceptions.ErrorCodeException;
import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import in.auto.jira.common.interfaces.ReptileService;
import in.auto.jira.common.model.LogWorkReq;
import in.auto.jira.common.utils.Tools;
import in.auto.jira.common.utils.okhttp.client.OkHttpSimpleClient;
import in.auto.jira.common.utils.okhttp.config.OkHttpConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

/**
 * 爬虫服务实现
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-02 17:13:14
 */
@Slf4j
@Service
public class ReptileServiceImpl implements ReptileService {
    @Autowired
    private OkHttpSimpleClient client;
    @Resource
    private JiraConfig config;
    @Resource
    OkHttpConfig okHttpConfig;

    // const
    public static final String USER_OR_PASSWORD_ERROR_FLAG = "Sorry, your username and password are incorrect - please try again.";
    public static final String PROJECT_FIELD_ID = "project";
    public static final String ISSUE_TYPE_FIELD_ID = "issuetype";
    public static final String CATEGORY_FIELD_ID = "customfield_11709";
    // 主线分类编号
    public static final String BUS_CATEGORY_FIELD_ID = "customfield_11712";
    public static final String ASSIGNEE_FIELD_ID = "assignee";
    public static final Map<String, Boolean> LOGIN_STATUS_FLAG = Maps.newHashMap();
    public static final Map<String, OkHttpClient> USER_CLIENT_MAP = Maps.newHashMap();
    // not log const
    public static final String CREATE_ISSUE_NOT_LOGIN_FLAG = "<h1>XSRF Security Token Missing</h1>";
    public static final String CREATE_ISSUE_TIMEOUT_LOGIN_FLAG = "<h1>Session Expired</h1>";
    public static final String QUERY_ISSUE_NOT_LOGIN_ERRMSG = "You are not logged in, and do not have the permissions required to create an issue in this project as a guest.";
    public static final Integer LOG_WORK_NOT_NOT_CODE = 401;
    public static final int RETRY_QTY = 3;

    @Override
    public IssueResult findQuickParams(String loginName, String password) {
        // 2.获取创建Issue所需请求参数
        return this.getIssueParams(loginName, password, RETRY_QTY);
    }

    private void login(String loginName, String password) {
        Map<String, Object> params = Maps.newHashMap();
        // fixed params
        params.put("os_destination", "/secure/BrowseProjects.jspa?selectedCategory=all");
        params.put("user_role", "");
        params.put("atl_token", "");
        params.put("login", "Log In");
        // params
        params.put("os_username", loginName);
        params.put("os_password", password);
        OkHttpClient currentClient = this.getCurrentClient(loginName, true);
        String strResult = client.postClient.postByFormDataResp2String(this.getReqUrl(config.getApis().getLogin()),
                params, currentClient);
        if (strResult.contains(USER_OR_PASSWORD_ERROR_FLAG)) {
            throw ErrorCodeExceptionFactory.build("用户名或密码错误，行不行啊？老弟", 999);
        }
        LOGIN_STATUS_FLAG.put(loginName, true);
    }

    private IssueResult getIssueParams(String loginName, String password, int retryQty) {
        if (!LOGIN_STATUS_FLAG.getOrDefault(loginName, false)) {
            this.login(loginName, password);
        }
        OkHttpClient currentClient = this.getCurrentClient(loginName, false);
        IssueResult result = null;
        try {
            result = client.postClient.postResp2ObjFromJson(this.getReqUrl(config.getApis().getGetIssueParams()),
                    IssueResult.class, currentClient);
        } catch (ErrorCodeException e) {
            // 未登录
            if (Objects.equal(e.getErrorCode().getStatus(), LOG_WORK_NOT_NOT_CODE)) {
                if (retryQty > 0) {
                    LOGIN_STATUS_FLAG.put(loginName, false);
                    return this.getIssueParams(loginName, password, --retryQty);
                } else {
                    throw ErrorCodeExceptionFactory.build("自动登录失败（重试了三次仍失败），请检查是否密码是否发生修改！", 702);
                }
            } else {
                throw e;
            }
        }
        if (!Tools.isNullOrEmpty(result.getErrorMessages())) {
            for (String item : result.getErrorMessages()) {
                // 未登录
                if (item.equals(QUERY_ISSUE_NOT_LOGIN_ERRMSG)) {
                    if (retryQty > 0) {
                        LOGIN_STATUS_FLAG.put(loginName, false);
                        this.getIssueParams(loginName, password, --retryQty);
                    } else {
                        throw ErrorCodeExceptionFactory.build("自动登录失败（重试了三次仍失败），请检查是否密码是否发生修改！", 702);
                    }
                }
                log.error("爬虫获取Issue参数时错误，错误信息：{}", item);
                throw ErrorCodeExceptionFactory.build(Tools.fillPlaceholder("获取Issue信息时错误：{}", item), 999);
            }
        }
        // 解析数据
        for (Field field : result.getFields()) {
            if (Objects.equal(field.getId(), PROJECT_FIELD_ID)) {
                this.analysisProject(result, field);
            } else if (Objects.equal(field.getId(), ISSUE_TYPE_FIELD_ID)) {
                // 问题类型
                this.analysisIssueType(result, field);
            } else if (Objects.equal(field.getId(), CATEGORY_FIELD_ID)) {
                // 分类&子分类
                this.analysisCategory(result, field);
            } else if (Objects.equal(field.getId(), BUS_CATEGORY_FIELD_ID)) {
                // 主线分类&子分类
                this.analysisCategory(result, field);
            } else if (Objects.equal(field.getId(), ASSIGNEE_FIELD_ID)) {
                // 经办人
                this.analysisAssignee(result, field);
            }
        }
        return result;
    }

    /**
     * 解析项目数据
     * 
     * @param result 返回值对象
     * @param field  项目相关数据信息
     */
    private void analysisProject(IssueResult result, Field field) {
        // 项目
        List<IssueResult.Element> projects = Lists.newArrayList();
        if (!Tools.isNullOrEmpty(field.getEditHtml())) {
            Document doc = Jsoup.parse(field.getEditHtml());
            Elements eles = doc.select("#project-options");
            if (!Tools.isNullOrEmpty(eles)) {
                Element projectNode = eles.get(0);
                JSONArray projectJSONArray = JSON.parseArray(projectNode.attr("data-suggestions"));
                for (Object object : projectJSONArray) {
                    JSONObject obj = (JSONObject) object;
                    String lable = obj.getString("label");
                    if (lable.equals("所有项目") || lable.equals("All Projects")) {
                        JSONArray procjets = obj.getJSONArray("items");
                        for (Object projectObj : procjets) {
                            JSONObject projectJSONObj = (JSONObject) projectObj;
                            projects.add(IssueResult.Element.builder().id(projectJSONObj.getString("value"))
                                    .name(projectJSONObj.getString("label")).build());
                        }
                    }
                }
            }
        }
        result.setProjects(projects);
    }

    /**
     * 解析问题类型
     * 
     * @param result 返回值对象
     * @param field  问题类型数据信息
     */
    private void analysisIssueType(IssueResult result, Field field) {
        Map<String, List<IssueResult.Element>> issueTypes = Maps.newHashMap();
        if (!Tools.isNullOrEmpty(field.getEditHtml())) {
            Document doc = Jsoup.parse(field.getEditHtml());
            Elements eles = doc.select("#issuetype-options");
            Map<String, JSONArray> issueMap = Maps.newHashMap();
            if (!Tools.isNullOrEmpty(eles)) {
                Element projectNode = eles.get(0);
                JSONArray issueTypeArray = JSON.parseArray(projectNode.attr("data-suggestions"));
                for (Object item : issueTypeArray) {
                    JSONObject issue = (JSONObject) item;
                    issueMap.put(issue.getString("label"), issue.getJSONArray("items"));
                }
            }
            Elements maps = doc.select("#issuetype-projects");
            if (!Tools.isNullOrEmpty(eles)) {
                Element projectIssueMapStr = maps.get(0);
                JSONObject projectIssueMap = JSON.parseObject(projectIssueMapStr.html());
                for (IssueResult.Element project : result.getProjects()) {
                    String issueTypeLabel = projectIssueMap.getString(project.getId());
                    if (!Tools.isNullOrEmpty(issueTypeLabel)) {
                        JSONArray issues = issueMap.get(issueTypeLabel);
                        if (!Tools.isNullOrEmpty(issues)) {
                            List<IssueResult.Element> issueList = Lists.newArrayList();
                            for (Object item : issues) {
                                JSONObject issue = (JSONObject) item;
                                issueList.add(IssueResult.Element.builder().id(issue.getString("value"))
                                        .name(issue.getString("label")).build());
                            }
                            issueTypes.put(project.getId(), issueList);
                        }
                    }
                }
            }
        }
        result.setIssueTypes(issueTypes);
    }

    /**
     * 解析分类数据
     * 
     * @param result 返回值对象
     * @param field  分类相关数据信息
     */
    private void analysisCategory(IssueResult result, Field field) {
        result.setExt(field.getId());
        List<IssueResult.Element> categorys = Lists.newArrayList();
        Map<String, List<IssueResult.Element>> subCategorys = Maps.newHashMap();
        if (!Tools.isNullOrEmpty(field.getEditHtml())) {
            Document doc = Jsoup.parse(field.getEditHtml());
            Elements eles = doc.select("#" + field.getId() + " option");
            for (Element element : eles) {
                if (!Tools.isNullOrEmpty(element.val())) {
                    categorys.add(IssueResult.Element.builder().id(element.val()).name(element.text()).build());
                    Elements subs = doc.select("option.option-group-" + element.val() + "");
                    List<IssueResult.Element> values = Lists.newArrayList();
                    for (Element sub : subs) {
                        if (!sub.val().equals(element.val())) {
                            if (!Tools.isNullOrEmpty(sub.val())) {
                                values.add(IssueResult.Element.builder().id(sub.val()).name(sub.text()).build());
                            }
                        }
                    }
                    subCategorys.put(element.val(), values);
                }
            }
        }
        result.setCategorys(categorys);
        result.setSubCategorys(subCategorys);
    }

    /**
     * 解析经办人数据
     * 
     * @param result 返回值对象
     * @param field  经办人相关数据信息
     */
    private void analysisAssignee(IssueResult result, Field field) {
        if (!Tools.isNullOrEmpty(field.getEditHtml())) {
            Document doc = Jsoup.parse(field.getEditHtml());
            Elements eles = doc.select(".current-user");
            if (!Tools.isNullOrEmpty(eles)) {
                Element element = eles.get(0);
                result.setAssignee(element.val());
            }
        }
    }

    @Override
    public void automation(BusDomain busDomain) {
        List<LogWorkDomain> logWorkDomains = busDomain.getWorks();
        // 循环创建Issue并Log工时
        for (LogWorkDomain logWorkDomain : logWorkDomains) {
            this.createIssue(busDomain.getLoginName(), logWorkDomain);
            this.log(busDomain.getLoginName(), logWorkDomain);
        }
    }

    /**
     * 创建issue
     * 
     * @param logWorkDomain logWork领域模型
     * @return
     */
    private IssueResult createIssue(String loginName, LogWorkDomain logWorkDomain) {
        Map<String, Object> params = Maps.newHashMap();
        // fixed params
        params.put("description", "");
        // 优先级，默认
        params.put("priority", 3);
        params.put("customfield_11833", "");
        params.put("dnd-dropzone", "");
        params.put("customfield_10800", "");
        params.put("fieldsToRetain", "project");
        params.put("fieldsToRetain", "issuetype");
        params.put("fieldsToRetain", "priority");
        params.put("fieldsToRetain", logWorkDomain.getCategoryType());
        params.put("fieldsToRetain", "assignee");
        params.put("fieldsToRetain", "customfield_11833");
        params.put("fieldsToRetain", "customfield_10800");
        // params
        params.put("pid", logWorkDomain.getProjectId());
        params.put("issuetype", logWorkDomain.getIssueTypeId());
        params.put("atl_token", logWorkDomain.getAtlToken());
        params.put("formToken", logWorkDomain.getFormToken());
        params.put("summary", logWorkDomain.getSummary());
        params.put(logWorkDomain.getCategoryType(), logWorkDomain.getCategoryId());
        params.put(logWorkDomain.getCategoryType() + ":1", logWorkDomain.getSubCategoryId());
        params.put("assignee", logWorkDomain.getAssignee());
        OkHttpClient currentClient = this.getCurrentClient(loginName, false);
        IssueResult result = client.postClient.postByFormDataResp2ObjFromJson(
                this.getReqUrl(config.getApis().getCreateIssue()), params, IssueResult.class, currentClient);
        logWorkDomain.setIssueId(result.getCreatedIssueDetails().getId());
        return result;
    }

    /**
     * log工时
     * 
     * @param logWorkDomain logWork领域模型
     */
    private void log(String loginName, LogWorkDomain logWorkDomain) {
        LogWorkReq params = LogWorkReq.builder().worker(logWorkDomain.getAssignee().toLowerCase())
                .originTaskId(logWorkDomain.getIssueId()).started(logWorkDomain.getLogDate())
                .timeSpentSeconds(new Long(logWorkDomain.getHours() * 60 * 60)).includeNonWorkingDays(false)
                .billableSeconds("").attributes(new Object()).build();
        String json = JSON.toJSONString(params, SerializerFeature.WriteMapNullValue);
        try {
            OkHttpClient currentClient = this.getCurrentClient(loginName, false);
            String logResult = client.postClient.postByJsonResp2String(this.getReqUrl(config.getApis().getLogWork()),
                    json, currentClient);
            log.info("log工时返回值：{}", logResult);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ErrorCodeExceptionFactory.build(Tools.fillPlaceholder("issue创建成功但工时log失败！错误信息：{}", e.getMessage()),
                    999);
        }
    }

    @Override
    public void logWorkAutomation(BusDomain busDomain) {
        this.login(busDomain.getLoginName(), busDomain.getPassword());
        for (LogWorkDomain item : busDomain.getWorks()) {
            this.log(busDomain.getLoginName(), item);
        }
    }

    /**
     * 获取请求地址
     * 
     * @param apiUrl api地址
     * @return
     */
    private String getReqUrl(String apiUrl) {
        return new StringBuilder().append(config.getDomain()).append(apiUrl).toString();
    }

    private synchronized OkHttpClient getCurrentClient(String loginName, boolean force) {
        if (!USER_CLIENT_MAP.containsKey(loginName)) {
            USER_CLIENT_MAP.put(loginName, okHttpConfig.newClient());
        } else {
            if (force) {
                USER_CLIENT_MAP.remove(loginName);
                USER_CLIENT_MAP.put(loginName, okHttpConfig.newClient());
            }
        }
        return USER_CLIENT_MAP.get(loginName);
    }
}
