package in.auto.jira.rest.controller;

import java.io.UnsupportedEncodingException;

import com.google.common.base.Charsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.auto.jira.common.domain.BusDomain;
import in.auto.jira.common.domain.IssueResult;
import in.auto.jira.common.interfaces.ReptileService;
import in.auto.jira.common.model.Result;
import in.auto.jira.rest.utils.ControllerUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 控制器
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-12-07 11:57:42
 */
@Slf4j
@RestController
@RequestMapping("/jira")
public class JiraController {
	@Autowired
	private ReptileService reptileService;

	@GetMapping("/issue/info")
	public Result<IssueResult> issueInfo(String loginName, String password) {
		String realPassword;
		try {
			realPassword = new String(Base64Utils.decodeFromString(password), Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			log.error("密码解码错误：{}", e.getMessage());
			return ControllerUtils.fail("密码加密错误", 999);
		}
		return ControllerUtils.success(reptileService.findQuickParams(loginName, realPassword));
	}

	@PostMapping("/issue/automation")
	public Result<Void> automation(@RequestBody BusDomain busDomain) {
		try {
			busDomain.setPassword(
					new String(Base64Utils.decodeFromString(busDomain.getPassword()), Charsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			log.error("密码解码错误：{}", e.getMessage());
			return ControllerUtils.fail("密码加密错误", 999);
		}
		reptileService.automation(busDomain);
		return ControllerUtils.success();
	}

	@PostMapping("/issue/log/automation")
	public Result<Void> logAutomation(@RequestBody BusDomain busDomain) {
		try {
			busDomain.setPassword(
					new String(Base64Utils.decodeFromString(busDomain.getPassword()), Charsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			log.error("密码解码错误：{}", e.getMessage());
			return ControllerUtils.fail("密码加密错误", 999);
		}
		reptileService.logWorkAutomation(busDomain);
		return ControllerUtils.success();
	}
}
