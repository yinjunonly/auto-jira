package in.auto.jira.common.model;

import in.auto.jira.common.enums.ErrorLevel;
import in.auto.jira.common.exceptions.ErrorCode;
import in.auto.jira.common.utils.Tools;
import com.google.common.base.Objects;

/**
 * rest 服务 返回值
 * 
 * @author yinju
 * @date 2017年12月11日下午2:02:37
 * @param <T（数据类型）>
 */
public class RestResult<T> implements Result<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T data = null;
	private String message = "成功";
	private static final Integer SUCCESS_STATUS = 200;
	private Integer status = 200;
	private ErrorLevel errorLevel = null;

	@Override
	public void setData(T data) {
		this.data = data;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setStatus(Integer status) {
		this.status = status;
		if (Objects.equal(this.status, SUCCESS_STATUS) && Tools.isNull(this.errorLevel)) {
			this.errorLevel = ErrorLevel.NONE;
		}
	}

	@Override
	public void setErrorCode(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
		if (Tools.isNull(this.errorLevel)) {
			this.errorLevel = ErrorLevel.E;
		}
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Integer getStatus() {
		return status;
	}

	@Override
	public void setErrorLevel(ErrorLevel level) {
		this.errorLevel = level;
	}

	@Override
	public ErrorLevel getErrorLevel() {
		return this.errorLevel;
	}
}