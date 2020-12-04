package in.auto.jira.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.auto.jira.common.domain.BusDomain;
import in.auto.jira.common.interfaces.ReptileService;
import in.auto.jira.common.model.Result;
import in.auto.jira.rest.utils.ControllerUtils;

@RestController
@RequestMapping("/jira")
public class JiraController {
	@Autowired
	private ReptileService reptileService;

	@GetMapping("/log/work")
	public Result<Void> logWork() {
		BusDomain busDomain = new BusDomain();
		busDomain.setLoginName("In.Yin");
		busDomain.setPassword("Yj@1217cb");
		reptileService.logWork(busDomain);
		return ControllerUtils.success();
	}
}
