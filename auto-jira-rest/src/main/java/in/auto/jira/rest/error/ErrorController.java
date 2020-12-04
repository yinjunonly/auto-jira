package in.auto.jira.rest.error;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import in.auto.jira.common.constants.FileConstant;
import in.auto.jira.common.enums.RestErrorCode;
import in.auto.jira.common.enums.SystemErrorCode;
import in.auto.jira.common.exceptions.ErrorCode;
import in.auto.jira.common.exceptions.ErrorCodeException;
import in.auto.jira.common.exceptions.NoRollbackErrorCodeException;
import in.auto.jira.common.model.Result;
import in.auto.jira.common.utils.Tools;
import in.auto.jira.rest.config.RestConfig;
import in.auto.jira.rest.utils.ControllerUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

/**
 * 默认错误处理Controller
 * 
 * @author yinju
 * @date 2018年6月4日下午12:05:06
 */
@Controller
public class ErrorController extends BasicErrorController {
	/** rest配置信息 */
	@Resource
	private RestConfig restConfig;

	public ErrorController(ServerProperties serverProperties) {
		super(new ErrorAttributes(), serverProperties.getError());
	}

	private static final String statusTag = "status=";
	private static final String messageTag = "message=";

	/**
	 * 覆盖全局错误
	 */
	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		// 输出自定义的Json格式
		Throwable throwable = (Throwable) body.get("throwable");
		Map<String, Object> resultMap = Maps.newHashMap();
		if (throwable instanceof ErrorCodeException) {
			Result<String> result = ControllerUtils.fail(((ErrorCodeException) throwable).getErrorCode());
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
			for (Entry<String, Object> item : jsonObject.entrySet()) {
				resultMap.put(item.getKey(), item.getValue());
			}
		} else {
			HttpStatus status = getStatus(request);
			if (HttpStatus.NOT_FOUND == status) {
				Result<String> result = ControllerUtils.fail(RestErrorCode.REQUEST_NOT_FIND_ERROR);
				JSONObject jsonObject = JSON
						.parseObject(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
				for (Entry<String, Object> item : jsonObject.entrySet()) {
					resultMap.put(item.getKey(), item.getValue());
				}
			} else if (!Tools.isNullOrEmpty(throwable.getMessage())) {
				String errorMessage = throwable.getMessage();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(errorMessage.getBytes(Charsets.UTF_8)), Charsets.UTF_8));
				try {
					String firstLine = bufferedReader.readLine();
					if (firstLine.indexOf(ErrorCodeException.class.getName()) > -1
							|| firstLine.indexOf(NoRollbackErrorCodeException.class.getName()) > -1) {
						int statusIndex = firstLine.indexOf(statusTag);
						int messageIndex = firstLine.indexOf(messageTag);
						String errorStatus = firstLine.substring(statusIndex + statusTag.length(),
								firstLine.indexOf(",", statusIndex));
						String message = firstLine.substring(messageIndex + messageTag.length(),
								firstLine.lastIndexOf(")"));
						Result<String> result = ControllerUtils.fail(message, Integer.parseInt(errorStatus));
						JSONObject jsonObject = JSON
								.parseObject(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
						for (Entry<String, Object> item : jsonObject.entrySet()) {
							resultMap.put(item.getKey(), item.getValue());
						}
					} else {
						this.serverAbnormalError(resultMap);
					}
				} catch (IOException e) {
					this.serverAbnormalError(resultMap);
				}
			} else {
				this.serverAbnormalError(resultMap);
			}
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	/**
	 * 设置服务器内部错误
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param resultMap
	 */
	private void serverAbnormalError(Map<String, Object> resultMap) {
		Result<String> result = ControllerUtils.fail(SystemErrorCode.SERVER_ABNORMAL_ERROR);
		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
		for (Entry<String, Object> item : jsonObject.entrySet()) {
			resultMap.put(item.getKey(), item.getValue());
		}
	}

	/**
	 * 覆盖默认的HTML响应
	 */
	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		// 获取原始请求
		String originalURL = (String) request.getAttribute("javax.servlet.forward.request_uri");
		// 请求的状态
		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		Throwable throwable = (Throwable) body.get("throwable");
		if (throwable instanceof ErrorCodeException) {
			ErrorCode code = ((ErrorCodeException) throwable).getErrorCode();
			// 如果是未找到界面并且是混入spa界面，则直接返回spa首页
			if (code == RestErrorCode.REQUEST_NOT_FIND_ERROR && restConfig.isMixinFront() && restConfig.isSpa()) {
				return new ModelAndView(this.getSinglePageDefaultPage(originalURL));
			} else {
				Map<String, Object> model = Collections.unmodifiableMap(
						getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
				response.setStatus(code.getStatus());
				ModelAndView modelAndView = resolveErrorView(request, response, HttpStatus.resolve(code.getStatus()),
						model);
				return modelAndView;
			}
		}
		// 指定自定义的视图
		HttpStatus status = getStatus(request);
		if (status == HttpStatus.NOT_FOUND && restConfig.isMixinFront() && restConfig.isSpa()) {
			return new ModelAndView(this.getSinglePageDefaultPage(originalURL));
		} else {
			Map<String, Object> model = Collections
					.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
			response.setStatus(status.value());
			ModelAndView modelAndView = resolveErrorView(request, response, status, model);
			return modelAndView;
		}
	}

	/**
	 * 获取单页应用默认界面
	 * 
	 * @change 2019年3月25日23:36:56 ，从配置文件中获取单页应用的根目录
	 * @author yinjun(yinjunonly@163.com)
	 * @param originalURL
	 * @return
	 */
	private String getSinglePageDefaultPage(String originalURL) {
		if (!restConfig.getFrontApps().isEmpty()) {
			for (String appName : restConfig.getFrontApps()) {
				if (originalURL.toLowerCase().startsWith(appName.toLowerCase())) {
					return new StringBuilder(appName).append(FileConstant.FILE_SEPARATOR)
							.append(restConfig.getDefaultPage()).toString();
				}
			}
		}
		return new StringBuilder(FileConstant.FILE_SEPARATOR).append(restConfig.getDefaultPage()).toString();
	}
}
