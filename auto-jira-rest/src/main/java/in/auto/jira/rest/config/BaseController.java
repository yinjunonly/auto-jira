package in.auto.jira.rest.config;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import in.auto.jira.common.enums.RestErrorCode;
import in.auto.jira.common.enums.SystemErrorCode;
import in.auto.jira.common.exceptions.ErrorCodeException;
import in.auto.jira.common.model.Result;
import in.auto.jira.rest.utils.ControllerUtils;

/**
 * @author lichao 2020/4/29
 */
public class BaseController {

	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 参数校验.
	 *
	 * @param result the result
	 */
	public void paramAuth(BindingResult result) throws ErrorCodeException {
		if (result.hasErrors()) {
			logger.info("[param] error, because of " + result.getFieldError());
			throw new ErrorCodeException(RestErrorCode.REQUEST_PARAMETER_ERROR);
		}
	}

	/**
	 * Fail result.
	 *
	 * @param <T> the generic type
	 * @param e the e
	 * @return the result< t>
	 */
	@ExceptionHandler
	public <T> Result<T> failResult(Exception e) {
		if (e instanceof ErrorCodeException) {
			return ControllerUtils.fail(((ErrorCodeException) e).getErrorCode());
		} else if (e instanceof ClientAbortException) {
			logger.info("发生外部错误, because of=[{用户主动关闭链接，0ClientAbortException}]");
			return null;
		} else if (e instanceof IllegalStateException) {
			logger.info("发生外部错误, because of=[{" + e.getMessage() + "}]");
			return null;
		} else if (e instanceof MissingServletRequestParameterException) {
			logger.info("发生外部错误, because of=[{用户传参数错误，MissingServletRequestParameterException}]");
			return null;
		} else if (e instanceof BindException) {
			logger.info("发生系统错误,用户IP=[{}]");
			return ControllerUtils.fail(SystemErrorCode.SERVER_ABNORMAL_ERROR);
		} else {
			if (e.getCause() instanceof ErrorCodeException) {
				return ControllerUtils.fail(((ErrorCodeException) e).getErrorCode());
			}
			logger.warn("发生系统错误,用户IP=[{}]", e);
			return ControllerUtils.fail(SystemErrorCode.SERVER_ABNORMAL_ERROR);
		}
	}
}
