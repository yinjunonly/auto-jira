package in.auto.jira.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
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
import in.auto.jira.common.domain.IssueResult.Field;
import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import in.auto.jira.common.interfaces.ReptileService;
import in.auto.jira.common.utils.ReflectionUtils;
import in.auto.jira.common.utils.Tools;
import in.auto.jira.common.utils.okhttp.client.OkHttpSimpleClient;
import lombok.extern.slf4j.Slf4j;

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

    // const
    public static final String NOT_LOGIN_FLAG = "Sorry, your username and password are incorrect - please try again.";
    public static final String PROJECT_FIELD_ID = "project";
    public static final String ISSUE_TYPE_FIELD_ID = "issuetype";
    public static final String CATEGORY_FIELD_ID = "customfield_11709";

    @Override
    public void logWork(BusDomain busDomain) {
        // 1.登录
        this.login(busDomain);
        // 2.获取创建Issue所需请求参数
        this.getIssueParams();
    }

    private void login(BusDomain busDomain) {
        Map<String, Object> params = Maps.newHashMap();
        // fixed params
        params.put("os_destination", "/secure/BrowseProjects.jspa?selectedCategory=all");
        params.put("user_role", "");
        params.put("atl_token", "");
        params.put("login", "Log In");
        // params
        params.put("os_username", busDomain.getLoginName());
        params.put("os_password", busDomain.getPassword());
        String strResult = client.postClient.postByFormDataResp2String(this.getReqUrl(config.getApis().getLogin()),
                params);
        if (strResult.contains(NOT_LOGIN_FLAG)) {
            throw ErrorCodeExceptionFactory.build("用户名或密码错误，行不行啊？老弟", 999);
        }
    }

    private void getIssueParams() {
        IssueResult result = client.postClient
                .postResp2ObjFromJson(this.getReqUrl(config.getApis().getGetIssueParams()), IssueResult.class);
        if (!Tools.isNullOrEmpty(result.getErrorMessages())) {
            for (String item : result.getErrorMessages()) {
                log.error("爬虫获取Issue参数时错误，错误信息：{}", item);
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
            }
        }
    }

    private String getReqUrl(String apiUrl) {
        return new StringBuilder().append(config.getDomain()).append(apiUrl).toString();
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
                    if (obj.getString("label").equals("所有项目")) {
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
            if (!Tools.isNullOrEmpty(eles)) {
                Element projectNode = eles.get(0);
                System.err.println(projectNode.attr("data-suggestions"));
                // JSONArray projectJSONArray =
                // JSON.parseArray(projectNode.attr("data-suggestions"));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse(new File("D:\\java_work\\auto-jira\\auto-jira-rest\\doc\\category.html"),
                Charsets.UTF_8.name());
        Elements eles = doc.select("#customfield_11709 option");
        for (Element element : eles) {
            System.err.println(element.text());
            System.err.println(element.val());
            Elements subs = doc.select("option.option-group-" + element.val() + "");
            for (Element sub : subs) {
                System.err.println("sub：" + sub.text());
            }
        }
    }
}
